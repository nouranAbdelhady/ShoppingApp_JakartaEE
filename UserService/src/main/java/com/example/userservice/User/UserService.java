package com.example.userservice.User;

import com.example.userservice.GeographicalRegion.GeographicalRegion;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.persistence.*;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Stateless
public class UserService {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("userr");

    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    private String accountServiceUrl= "http://localhost:8080/AccountService-1.0-SNAPSHOT/api/accounts";

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
            URL url = new URL(accountServiceUrl+"/"+name);
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
                URL url = new URL(accountServiceUrl+"/delete/"+userr.getUsername());
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


}
