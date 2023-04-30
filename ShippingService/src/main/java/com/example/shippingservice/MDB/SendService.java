package com.example.shippingservice.MDB;

import com.example.shippingservice.Notification.Notification;
import com.example.shippingservice.Notification.NotificationService;
import com.example.shippingservice.Shipping.ShippingCompany;
import com.example.shippingservice.Shipping.ShippingCompanyService;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Stateless
public class SendService {
    @Resource(lookup = "java:/jms/queue/ShippingRequestQueue")
    private Queue queue;

    @EJB
    NotificationService notificationService;

    @EJB
    ShippingCompanyService shippingCompanyService;

    private String customerServiceUrl= "http://localhost:16957/UserService-1.0-SNAPSHOT/api/users";

    public void sendMessage(String sender, String sentMessage, String receiver) {
        String toSendMessage = sender + "," + sentMessage + "," + receiver;
        try {
            Context context = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("java:/ConnectionFactory");
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(this.queue);
            ObjectMessage jmsMessage = session.createObjectMessage();
            jmsMessage.setObject(toSendMessage);
            producer.send(jmsMessage);
            System.out.println("Message sent to " + receiver);
            session.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Message not sent to " + receiver);
        }
    }

    public String requestShipping(String request, String customerUsername) {
        //Get the customer's address
        String customerUrl = customerServiceUrl + "/username/" + customerUsername;
        String regionName = "";
        try {
            URL url = new URL(customerUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            System.out.println("Connecting to URL: " + url); // Add this line
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            StringBuilder responseBuilder = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                responseBuilder.append(output);
                System.out.println("Output: " + output);
            }
            conn.disconnect();

            // Split at "name:"
            String[] responseArray = responseBuilder.toString().split("name\":");
            // Get the element after the last "name:"
            String region = responseArray[responseArray.length - 1];
            // Remove the last "}" and quotes
            regionName = region.substring(1, region.length() - 3);
            System.out.println("Region: " + regionName);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // Get shipping companies by region
        List<ShippingCompany> shippingCompanies = shippingCompanyService.getCompaniesByRegion(regionName);
        if(shippingCompanies.size() == 0){
            // No shipping company available
            String message = "No shipping company available";
            sendMessage("null", message, customerUsername);
            // Reject order (state=failed)
            return message;
        }
        else{
            // Shipping company available
            // System.out.println("Request: " + request);
            // Send message to shipping company
            for (ShippingCompany shippingCompany : shippingCompanies) {
                //sendMessage(customerUsername, request, shippingCompany);
                //System.out.println("Message sent to shipping company " + shippingCompany);
                notificationService.addNotification(new Notification(customerUsername, request, shippingCompany.getName()));
            }
            return request;
        }
    }

    public String updateShippingRequest(int notificationId, String response) {
        Notification notification = notificationService.getNotificationById(notificationId);
        if(notification == null){
            return "Notification not found";
        }
        String message = "";
        if(response.equals("YES")||response.equals("yes")){
            // Shipping company accepted the request
            // Send message to customer
            sendMessage(notification.getTargeted_username(), notification.getTargeted_username()+" accepted your shipping request", notification.getSender_username());

            // Remove same notification to other shipping companies
            List<Notification> notifications = notificationService.getSimilarRequests(notification.getMessage(), notification.getDate());
            for (Notification notification1 : notifications) {
                notificationService.deleteNotification(notification1.getId());
            }
            message = "Shipping company: "+notification.getTargeted_username()+" accepted your request";
        }else{
            // Shipping company rejected the request
            // Remove this notification
            notificationService.deleteNotification(notification.getId());

            // Check if there are other notifications with the same message and date
            List<Notification> notifications = notificationService.getSimilarRequests(notification.getMessage(), notification.getDate());
            if (notifications.size() == 0) {
                // No other notifications with the same message and date
                // Send message to customer
                sendMessage(notification.getTargeted_username(), "No shipping company available", notification.getSender_username());
                message = "No shipping company available";
            }
            else{
                // There are other notifications with the same message and date
                message = "Other shipping company available";
                //System.out.println("Size:"+notifications.size());
                //System.out.println("0:"+notifications.get(0).getId());
            }
        }
        return message;
    }
}