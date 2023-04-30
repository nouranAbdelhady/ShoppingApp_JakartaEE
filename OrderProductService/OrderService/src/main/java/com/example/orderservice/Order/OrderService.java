package com.example.orderservice.Order;

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
import java.util.List;

@Stateless
public class OrderService {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("order");

    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    private String userServiceUrl = "http://localhost:16957/UserService-1.0-SNAPSHOT/api/users";
    private String productServiceUrl = "http://localhost:9314/ProductService-1.0-SNAPSHOT/api/products";

    public List<Order> getAllOrders() {
        TypedQuery<Order> query = entityManager.createQuery("SELECT o FROM Order o", Order.class);
        List<Order> orders = query.getResultList();
        return orders;
    }

    public Order getOrderByID(int id) {
        return entityManager.find(Order.class, id);
    }

    public List<Order> getOrderByUsername(String username) {
        TypedQuery<Order> query = entityManager.createQuery("SELECT o FROM Order o WHERE o.username = :username", Order.class);
        query.setParameter("username", username);
        List<Order> orders = query.getResultList();
        return orders;
    }

    public void createOrder(Order order) {
        entityManager.getTransaction().begin();
        entityManager.persist(order);
        entityManager.getTransaction().commit();
    }

    public boolean deleteOrder(int Id) {
        Order targetedOrder = getOrderByID(Id);
        entityManager.getTransaction().begin();
        entityManager.remove(targetedOrder);
        entityManager.getTransaction().commit();
        return true;
    }

    public void updateOrder(Order order) {
        entityManager.getTransaction().begin();
        entityManager.merge(order);
        entityManager.getTransaction().commit();
    }

    public List<Order> getOrderByState(String state) {
        TypedQuery<Order> query = entityManager.createQuery("SELECT o FROM Order o WHERE o.state = :state", Order.class);
        query.setParameter("state", state);
        List<Order> orders = query.getResultList();
        return orders;
    }

    public String updateOrderState(int orderId, String state) {
        Order order = getOrderByID(orderId);
        if (order == null) {
            return "Order not found";
        }
        order.setState(state);
        updateOrder(order);
        // if state = 'shipping' then update product state to 'sold'
        if (state.equals("shipping")) {
            int productId = order.getProductId();
            System.out.println("update product ID: " + productId + " to sold");
            String productUrl = productServiceUrl + "/updateState/" + productId;
            try {
                URL url = new URL(productUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                System.out.println("Connecting to URL: " + url); // Add this line
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "text/plain");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true); // Enable output writing
                String input = "sold";
                System.out.println("Input: " + input);
                OutputStream os = conn.getOutputStream();
                os.write(input.getBytes());
                os.flush();
                if (conn.getResponseCode() != 200) {
                    System.out.println("Response code: " + conn.getResponseCode());
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

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return "Order state updated successfully";
    }

    public List<Order> getOrderByNameAndState(String username, String state) {
        TypedQuery<Order> query = entityManager.createQuery("SELECT o FROM Order o WHERE o.state = :state and o.username=:username", Order.class);
        query.setParameter("state", state);
        query.setParameter("username", username);
        List<Order> orders = query.getResultList();
        return orders;
    }

    public List<Order> getOrdersByProductId(int productId) {
        TypedQuery<Order> query = entityManager.createQuery("SELECT o FROM Order o WHERE o.productId = :productId", Order.class);
        query.setParameter("productId", productId);
        List<Order> orders = query.getResultList();
        return orders;
    }

    public List<List<String>> getCustomerDetailsbyUsername(String target) {
        try {
            URL url = new URL(userServiceUrl + "/username/" + target);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            System.out.println("Connecting to URL: " + url);
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
            String response = responseBuilder.toString();
            System.out.println("Response: " + response); // print the response string
            JSONObject jsonObject = new JSONObject(response);
            String username = jsonObject.getString("username");
            String fullname = jsonObject.getString("fullname");
            String email = jsonObject.getString("email");
            String password = jsonObject.getString("password");
            String type = jsonObject.getString("type");
            JSONObject geographicalRegion = jsonObject.getJSONObject("geographicalRegion");
            int geoId = geographicalRegion.getInt("id");
            String geoName = geographicalRegion.getString("name");
            // check if geoName ends with a certain character or substring
            if (geoName.endsWith("\"")) {
                geoName = geoName.substring(0, geoName.length() - 1);
            }
            List<String> customer = new ArrayList<>();
            customer.add(username);
            customer.add(fullname);
            customer.add(email);
            customer.add(password);
            customer.add(type);
            customer.add(String.valueOf(geoId));
            customer.add(geoName);
            List<List<String>> customers = new ArrayList<>();
            customers.add(customer);
            System.out.println("Customers: " + customers);
            return customers;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

