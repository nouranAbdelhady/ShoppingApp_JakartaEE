package com.example.shippingservice.MDB;

import com.example.shippingservice.Notification.Notification;
import com.example.shippingservice.Notification.NotificationService;
import jakarta.ejb.EJB;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;

@MessageDriven( activationConfig = {
        @jakarta.ejb.ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),
        @jakarta.ejb.ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/ShippingRequestQueue") })
public class MessageReceiver implements MessageListener {

    @EJB
    NotificationService notificationService;

    @Override
    public void onMessage(Message message) {
        try
        {
            System.out.println("New Message received");
            String messageBody = message.getBody(String.class);
            String [] messageBodyArray = messageBody.split(",");
            String sender_username = messageBodyArray[0];
            String response = messageBodyArray[1];
            String receiver_username = messageBodyArray[2];

            // Persist the notification
            System.out.println("Received message: " + messageBody);
            this.persistNotification(new Notification(sender_username, response, receiver_username, false));
        }
        catch (JMSException e)
        {
            e.printStackTrace();
        }
    }

    public void persistNotification(Notification notification) {
        notificationService.addNotification(notification);
        System.out.println("Notification persisted: "+notification.getId());
    }
}
