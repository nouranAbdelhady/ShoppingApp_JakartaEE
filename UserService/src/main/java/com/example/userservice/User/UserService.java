package com.example.userservice.User;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.persistence.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class UserService {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("userr");

    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    private String accountServiceUrl = "http://localhost:8080/AccountService-1.0-SNAPSHOT/api/accounts";

    private String orderServiceUrl = "http://localhost:8080/OrderService-1.0-SNAPSHOT/api";

    @PostConstruct
    public void testInit() {
        System.out.println("User service initialized");
    }

    public List<Userr> getAllUsers() {
        TypedQuery<Userr> query = entityManager.createQuery("SELECT c FROM Userr c", Userr.class);
        List<Userr> userrs = query.getResultList();
        return userrs;
    }

    public List<Userr> getAllCustomers() {
        TypedQuery<Userr> query = entityManager.createQuery("SELECT c FROM Userr c WHERE c.type ='Customer'", Userr.class);
        List<Userr> userrs = query.getResultList();
        return userrs;
    }

    public List<Userr> getAllAdmins() {
        TypedQuery<Userr> query = entityManager.createQuery("SELECT c FROM Userr c WHERE c.type ='Admin'", Userr.class);
        List<Userr> userrs = query.getResultList();
        return userrs;
    }

    public Userr getUserById(int id) {
        return entityManager.find(Userr.class, id);
    }


    public void addUser(Userr userr) {
        entityManager.getTransaction().begin();
        entityManager.persist(userr);
        entityManager.getTransaction().commit();

        // Add account using account service
        JsonObject account = Json.createObjectBuilder()
                .add("username", userr.getUsername())
                .add("password", userr.getPassword())
                .add("type", userr.getType())
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

    public boolean updateUser(String name, Userr userr) {
        Userr targetedUserr = getUserByUsername(name);
        targetedUserr.setGeographicalRegion(userr.getGeographicalRegion());
        targetedUserr.setType(userr.getType());
        targetedUserr.setUsername(userr.getUsername());
        targetedUserr.setEmail(userr.getEmail());
        targetedUserr.setPassword(userr.getPassword());
        targetedUserr.setFullname(userr.getFullname());
        entityManager.getTransaction().begin();
        Userr newUserr = entityManager.merge(targetedUserr);
        entityManager.getTransaction().commit();

        // Update account using account service
        JsonObject account = Json.createObjectBuilder()
                .add("username", userr.getUsername())
                .add("password", userr.getPassword())
                .add("type", userr.getType())
                .build();
        // Send request to account service
        try {
            URL url = new URL(accountServiceUrl + "/" + name);
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Check if merge is successful
        return newUserr != null;
    }

    public boolean deleteUser(Userr userr) {
        entityManager.getTransaction().begin();
        entityManager.remove(userr);
        entityManager.getTransaction().commit();
        // Check if remove is successful
        if (entityManager.find(Userr.class, userr.getId()) == null) {
            // Remove from account service
            try {
                URL url = new URL(accountServiceUrl + "/delete/" + userr.getUsername());
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
            return true;
        }
        return false;
    }

    public Userr getUserByUsername(String username) {
        TypedQuery<Userr> query = entityManager.createQuery("SELECT c FROM Userr c WHERE c.username = :username", Userr.class);
        query.setParameter("username", username);
        return query.getSingleResult();
    }

    public List<List<String>> getOrdersbyName(String target) {
        try {
            URL url = new URL(orderServiceUrl + "/orders/" + target);
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

            String response = responseBuilder.toString();
            JSONArray jsonArray = new JSONArray(response);

            List<List<String>> orders = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String username = jsonObject.getString("username");
                String productId = String.valueOf(jsonObject.getInt("productId"));
                String amount = String.valueOf(jsonObject.getDouble("amount"));
                String shipping_address = jsonObject.getString("shipping_address");
                String state = jsonObject.getString("state");

                // remove the extra " character from the end of the state field
                if (state.endsWith("\"")) {
                    state = state.substring(0, state.length() - 1);
                }

                List<String> order = new ArrayList<>();
                order.add(username);
                order.add(productId);
                order.add(amount);
                order.add(shipping_address);
                order.add(state);
                orders.add(order);
            }

            System.out.println("Orders: " + orders);
            return orders;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<List<String>> getPurchasedOrders(String name) {
        try {
            URL url = new URL(orderServiceUrl + "/orders/" + name);
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

            String response = responseBuilder.toString();
            JSONArray jsonArray = new JSONArray(response);

            List<List<String>> orders = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String username = jsonObject.getString("username");
                String productId = String.valueOf(jsonObject.getInt("productId"));
                String amount = String.valueOf(jsonObject.getDouble("amount"));
                String shipping_address = jsonObject.getString("shipping_address");
                String state = jsonObject.getString("state");

                // remove the extra " character from the end of the state field
                if (state.endsWith("\"")) {
                    state = state.substring(0, state.length() - 1);
                }

                if (state.equals("purchased")) {
                    List<String> order = new ArrayList<>();
                    order.add(username);
                    order.add(productId);
                    order.add(amount);
                    order.add(shipping_address);
                    order.add(state);
                    orders.add(order);
                }
            }

            System.out.println("Orders: " + orders);
            return orders;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<List<String>> getCurrentOrders(String name) {
        try {
            URL url = new URL(orderServiceUrl + "/orders/" + name);
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

            String response = responseBuilder.toString();
            JSONArray jsonArray = new JSONArray(response);

            List<List<String>> orders = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String username = jsonObject.getString("username");
                String productId = String.valueOf(jsonObject.getInt("productId"));
                String amount = String.valueOf(jsonObject.getDouble("amount"));
                String shipping_address = jsonObject.getString("shipping_address");
                String state = jsonObject.getString("state");

                // remove the extra " character from the end of the state field
                if (state.endsWith("\"")) {
                    state = state.substring(0, state.length() - 1);
                }

                if (state.equals("created")) {
                    List<String> order = new ArrayList<>();
                    order.add(username);
                    order.add(productId);
                    order.add(amount);
                    order.add(shipping_address);
                    order.add(state);
                    orders.add(order);
                }
            }

            System.out.println("Orders: " + orders);
            return orders;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

