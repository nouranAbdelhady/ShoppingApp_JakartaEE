package com.example.productservice.SellingCompany;

import com.example.productservice.Product.Product;
import jakarta.ejb.Stateless;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.persistence.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Stateless
public class SellingCompanyService {

    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("product");

    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    private String accountServiceUrl = "http://localhost:16957/AccountService-1.0-SNAPSHOT/api/accounts";

    private String notificationServiceUrl = "http://localhost:9314/OrderService-1.0-SNAPSHOT/api/notifactions";

    private String orderServiceUrl = "http://localhost:9314/OrderService-1.0-SNAPSHOT/api/orders";
    private String shippingServiceUrl = "http://localhost:8080/ShippingService-1.0-SNAPSHOT/api/send";

    public void addSellingCompany(SellingCompany sellingCompany) {
        entityManager.getTransaction().begin();
        entityManager.persist(sellingCompany);
        entityManager.getTransaction().commit();
        ;
        // Add account using account service
        JsonObject account = Json.createObjectBuilder()
                .add("username", sellingCompany.getName())
                .add("password", sellingCompany.getPassword())
                .add("type", "Selling_Company")
                .build();
        System.out.println(account);
        // Send request to account service
        try {
            URL url = new URL(accountServiceUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            // Write the account object to the request body
            String accountJson = account.toString();
            conn.getOutputStream().write(accountJson.getBytes());
            // Get the response code and response message
            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();
            System.out.println("Response code: " + responseCode);
            System.out.println("Response message: " + responseMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SellingCompany getSellingCompanyById(int id) {
        return entityManager.find(SellingCompany.class, id);
    }

    public SellingCompany getSellingCompanyByName(String name) {
        TypedQuery<SellingCompany> query = entityManager.createQuery("SELECT s FROM SellingCompany s WHERE s.name = :name", SellingCompany.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

    public List<SellingCompany> getAllSellingCompanies() {
        return entityManager.createQuery("SELECT s FROM SellingCompany s", SellingCompany.class).getResultList();
    }

    public SellingCompany updateSellingCompany(String targetedName, SellingCompany sellingCompany) {
        SellingCompany targetedSellingCompany = getSellingCompanyByName(targetedName);
        if (targetedSellingCompany == null) return null;
        // Update selling company using product service
        targetedSellingCompany.setName(sellingCompany.getName());
        targetedSellingCompany.setPassword(sellingCompany.getPassword());
        targetedSellingCompany.setProducts(targetedSellingCompany.getProducts());       //keep products
        entityManager.getTransaction().begin();
        entityManager.merge(targetedSellingCompany);
        entityManager.getTransaction().commit();
        // Update account using account service
        JsonObject account = Json.createObjectBuilder()
                .add("username", sellingCompany.getName())
                .add("password", sellingCompany.getPassword())
                .add("type", "Selling_Company")
                .build();
        System.out.println(account);
        // Send request to account service
        try {
            URL url = new URL(accountServiceUrl + "/" + targetedName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            // Write the account object to the request body
            String accountJson = account.toString();
            conn.getOutputStream().write(accountJson.getBytes());
            // Get the response code and response message
            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();
            System.out.println("Response code: " + responseCode);
            System.out.println("Response message: " + responseMessage);
            return targetedSellingCompany;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteSellingCompany(SellingCompany sellingCompany) {
        entityManager.getTransaction().begin();
        entityManager.remove(sellingCompany);
        entityManager.getTransaction().commit();
        // Delete account using account service
        try {
            URL url = new URL(accountServiceUrl + "/delete/" + sellingCompany.getName());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            // Get the response code and response message
            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();
            System.out.println("Response code: " + responseCode);
            System.out.println("Response message: " + responseMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addProductToSellingCompany(SellingCompany sellingCompany, Product product) {
        sellingCompany.addProduct(product);
        product.setSellingCompany(sellingCompany);
        entityManager.getTransaction().begin();
        entityManager.merge(sellingCompany);
        entityManager.getTransaction().commit();
    }

    public List<Product> getProductsBySellingCompany(String sellingCompanyName) {
        /*
        return entityManager.createQuery("SELECT p FROM Product p WHERE p.sellingCompany.name = :sellingCompanyName", Product.class)
                .setParameter("sellingCompanyName", sellingCompanyName)
                .getResultList();
    */
        SellingCompany sellingCompany = getSellingCompanyByName(sellingCompanyName);
        return sellingCompany.getProducts();
    }

    public boolean deleteProductFromSellingCompany(String sellingCompanyName, int productId) {
        SellingCompany sellingCompany = getSellingCompanyByName(sellingCompanyName);
        Product product = entityManager.createQuery("SELECT p FROM Product p WHERE p.id = :productId", Product.class)
                .setParameter("productId", productId)
                .getSingleResult();
        if (sellingCompany.getProducts().contains(product)) {
            sellingCompany.removeProduct(product);
            entityManager.getTransaction().begin();
            entityManager.merge(sellingCompany);
            //remove from products table
            entityManager.remove(product);
            entityManager.getTransaction().commit();
            return true;
        }
        return false;
    }

    public List<Product> getProductsBySellingCompanyAndState(String sellingCompanyName, String state) {
        return entityManager.createQuery("SELECT p FROM Product p WHERE p.sellingCompany.name = :sellingCompanyName AND p.state = :state", Product.class)
                .setParameter("sellingCompanyName", sellingCompanyName)
                .setParameter("state", state)
                .getResultList();
    }

    public SellingCompany getSellingCompanyByProductId(int productId) {
        String jpql = "SELECT s FROM SellingCompany s JOIN s.products p WHERE p.id = :productId";
        TypedQuery<SellingCompany> query = entityManager.createQuery(jpql, SellingCompany.class)
                .setParameter("productId", productId);
        List<SellingCompany> resultList = query.getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    // RepresentiveName
    public List<RepresentativeName> getAllRepresentativeNames() {
        return entityManager.createQuery("SELECT r FROM RepresentativeName r", RepresentativeName.class).getResultList();
    }

    public void addRepresentativeName(RepresentativeName representativeName) {
        entityManager.getTransaction().begin();
        entityManager.persist(representativeName);
        entityManager.getTransaction().commit();
    }

    // Get names of representatives that are not assigned to a selling company
    public List<RepresentativeName> getUnassignedRepresentativeNames() {
        return entityManager.createQuery("SELECT r FROM RepresentativeName r LEFT JOIN SellingCompany s ON r.name = s.name WHERE s.name IS NULL ", RepresentativeName.class).getResultList();
    }

    // Get names of representatives that are assigned to a selling company
    public List<RepresentativeName> getAssignedRepresentativeNames() {
        return entityManager.createQuery("SELECT r FROM RepresentativeName r LEFT JOIN SellingCompany s ON r.name = s.name WHERE s.name IS NOT NULL ", RepresentativeName.class).getResultList();
    }

    public List<List<String>> getNotification(String sellerUsername) {
        try {
            URL url = new URL(notificationServiceUrl + "/receiver/" + sellerUsername);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                responseBuilder.append(output);
            }
            conn.disconnect();
            String response = responseBuilder.toString();
            System.out.println("Response: " + response); // print the response string
            JSONArray jsonArray = new JSONArray(response);
            List<List<String>> notifications = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = String.valueOf(jsonObject.getInt("id"));
                String targetedUsername = jsonObject.getString("targeted_username");
                String message = jsonObject.getString("message");
                String senderUsername = jsonObject.getString("sender_username");
                String date = jsonObject.getString("date");
                String request = String.valueOf(jsonObject.getBoolean("request"));
                List<String> notification = Arrays.asList(id, targetedUsername, message, senderUsername, date, request);
                notifications.add(notification);
            }
            return notifications;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateSellingRequest(String sellerUsername, int sellingRequestId, String response) {
        try {
            System.out.println("Starting to update selling request with response " + response);
            // Get the notification for the selling request
            System.out.println("Getting notification for seller " + sellerUsername + " and selling request ID " + sellingRequestId);
            List<List<String>> notifications = getNotification(sellerUsername);
            if (notifications == null) {
                System.err.println("Failed to get notifications for seller " + sellerUsername);
                return;
            }
            String notificationId = null;
            String orderId = null;
            String customerUsername = null;
            for (List<String> notification : notifications) {
                if (notification.get(0).equals(String.valueOf(sellingRequestId))) {
                    notificationId = notification.get(0);
                    //Get order id from message
                    String[] messageArray = notification.get(2).split("ID");
                    System.out.println("notification.get(0): " + notification.get(0));
                    System.out.println("notification.get(2): " + notification.get(2));
                    System.out.println("messageArray: " + Arrays.toString(messageArray));
                    if (messageArray.length < 2) {
                        System.err.println("Error: messageArray does not have enough elements");
                        return;
                    }
                    orderId = messageArray[1].trim();
                    orderId = orderId.replace(".", "");
                    System.out.println("Order id: " + orderId);
                    customerUsername = notification.get(3);
                    System.out.println("Customer username:" + customerUsername);
                    break;
                }
            }
            if (notificationId == null) {
                System.err.println("Error: notification not found for selling request ID " + sellingRequestId);
                return;
            }
            // Update the selling request
            System.out.println("Updating selling request with response " + response);
            String state;
            if (response.equalsIgnoreCase("yes")) {
                state = "processing";
                // send notification to customer
                URL urlNotification = new URL(notificationServiceUrl );
                JsonObject notification = Json.createObjectBuilder()
                        .add("targeted_username", customerUsername)
                        .add("sender_username", sellerUsername)
                        .add("request", false)
                        .add("message","Your order: "+orderId+" is accepted!")
                        .add("date", java.time.LocalDate.now().toString())
                        .build();
                System.out.println(notification);
                try {
                    HttpURLConnection conn = (HttpURLConnection) urlNotification.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);

                    // Write the notification object to the request body
                    String notificationJson = notification.toString();
                    conn.getOutputStream().write(notificationJson.getBytes());

                    // Get the response code and response message
                    int responseCode = conn.getResponseCode();
                    String responseMessage = conn.getResponseMessage();
                    System.out.println("Response code: " + responseCode);
                    System.out.println("Response message: " + responseMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }



                if (customerUsername != null) {
                    try {
                        // send request to shipping
                        URL url = new URL(shippingServiceUrl + "/request_shipping");
                        String message = "New shipping request with order id: " + orderId + ";" + customerUsername;

                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "text/plain");
                        conn.setRequestProperty("Accept", "application/json");
                        conn.setDoOutput(true);

                        String input = message;
                        OutputStream os = conn.getOutputStream();
                        os.write(input.getBytes());
                        os.flush();

                        // Get the response code and response message
                        int responseCode = conn.getResponseCode();
                        String responseMessage = conn.getResponseMessage();
                        System.out.println("Response code: " + responseCode);
                        System.out.println("Response message: " + responseMessage);

                        if (conn.getResponseCode() != 200) {
                            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                        }else{
                            System.out.println("Request shipping sent");
                        }

                        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

                        StringBuilder responseBuilder = new StringBuilder();
                        String output;
                        while ((output = br.readLine()) != null) {
                            responseBuilder.append(output);
                            System.out.println("Output: " + output);
                        }
                        conn.disconnect();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            } else {
                state = "failed";

                // send notification to customer
                URL urlNotification = new URL(notificationServiceUrl );
                JsonObject notification = Json.createObjectBuilder()
                        .add("targeted_username", customerUsername)
                        .add("sender_username", sellerUsername)
                        .add("request", false)
                        .add("message","Your order: "+orderId+" is rejected!")
                        .add("date", java.time.LocalDate.now().toString())
                        .build();
                System.out.println(notification);
                try {
                    HttpURLConnection conn = (HttpURLConnection) urlNotification.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);

                    // Write the notification object to the request body
                    String notificationJson = notification.toString();
                    conn.getOutputStream().write(notificationJson.getBytes());

                    // Get the response code and response message
                    int responseCode = conn.getResponseCode();
                    String responseMessage = conn.getResponseMessage();
                    System.out.println("Response code: " + responseCode);
                    System.out.println("Response message: " + responseMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            URL url = new URL(orderServiceUrl + "/updateState/" + orderId);
            System.out.println("Connecting to URL: " + url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            os.write(state.getBytes());
            os.flush();
            os.close();
            System.out.println("Response Code: " + conn.getResponseCode());
            conn.disconnect();

            // remove notification
            System.out.println("Removing notification with ID " + notificationId);
            URL urlNotification = new URL(notificationServiceUrl + "/" + notificationId);
            System.out.println("Connecting to URL: " + urlNotification);
            HttpURLConnection connNotification = (HttpURLConnection) urlNotification.openConnection();
            connNotification.setRequestMethod("DELETE");
            connNotification.setRequestProperty("Content-Type", "application/json");
            connNotification.setRequestProperty("Accept", "application/json");
            connNotification.setDoOutput(true);
            System.out.println("Response Code: " + connNotification.getResponseCode());
            connNotification.disconnect();

        } catch (Exception e) {
            System.err.println("Failed to change state: " + e.getMessage());
        }
    }

}

