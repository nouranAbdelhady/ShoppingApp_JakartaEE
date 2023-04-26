package com.example.shippingservice.Shipping;

import jakarta.ejb.Stateless;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Stateless
public class ShippingCompanyService {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("shipping");

    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    private String regionServiceUrl = "http://localhost:8080/UserService-1.0-SNAPSHOT/api/regions";
    private String accountServiceUrl = "http://localhost:8080/AccountService-1.0-SNAPSHOT/api/accounts";

    public ShippingCompany addShippingCompany(ShippingCompany shippingCompany) {
        entityManager.getTransaction().begin();
        entityManager.persist(shippingCompany);
        entityManager.getTransaction().commit();
        // Add shipping company to account service
        JsonObject shippingCompanyAccount = Json.createObjectBuilder()
                .add("username", shippingCompany.getName())
                .add("password", shippingCompany.getPassword())
                .add("type", "Shipping_Company")
                .build();
        System.out.println(shippingCompanyAccount);
        // Send request to account service
        try {
            URL url = new URL(accountServiceUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            // Write the account object to the request body
            String shippingCompanyJson = shippingCompanyAccount.toString();
            conn.getOutputStream().write(shippingCompanyJson.getBytes());
            // Get the response code and response message
            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();
            System.out.println("Response code: " + responseCode);
            System.out.println("Response message: " + responseMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String companyName = shippingCompany.getName();
        TypedQuery<ShippingCompany> query = entityManager.createQuery("SELECT s FROM ShippingCompany s WHERE s.name = :companyName", ShippingCompany.class);
        query.setParameter("companyName", companyName);
        return query.getSingleResult();
    }

    public ShippingCompany getShippingCompanyByName(String name) {
        TypedQuery<ShippingCompany> query = entityManager.createQuery("SELECT s FROM ShippingCompany s WHERE s.name = :name", ShippingCompany.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

    public List<ShippingCompany> getAllShippingCompanies() {
        TypedQuery<ShippingCompany> query = entityManager.createQuery("SELECT s FROM ShippingCompany s", ShippingCompany.class);
        return query.getResultList();
    }

    public ShippingCompanyxRegion addRegionToShippingCompany(String shippingCompanyName, String regionName) {
        ShippingCompany shippingCompany = getShippingCompanyByName(shippingCompanyName);
        // Send request to region service (Create new region if it doesn't exist)
        // Add account using account service
        JsonObject region = Json.createObjectBuilder()
                .add("name", regionName)
                .build();
        System.out.println(region);
        // Send request to account service
        try {
            URL url = new URL(regionServiceUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            // Write the account object to the request body
            String regionJSON = region.toString();
            conn.getOutputStream().write(regionJSON.getBytes());
            // Get the response code and response message
            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();
            System.out.println("Response code: " + responseCode);
            System.out.println("Response message: " + responseMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Company name exists
        if (shippingCompany != null) {
            // region is not linked to company already
            TypedQuery<ShippingCompanyxRegion> query = entityManager.createQuery("SELECT s FROM ShippingCompanyxRegion s WHERE s.shippingCompanyName = :shippingCompanyName AND s.region = :region", ShippingCompanyxRegion.class);
            query.setParameter("shippingCompanyName", shippingCompanyName);
            query.setParameter("region", regionName);
            if (query.getResultList().size() == 0) {
                // Add region to company
                ShippingCompanyxRegion shippingCompanyxRegion = new ShippingCompanyxRegion(shippingCompanyName, regionName);
                entityManager.getTransaction().begin();
                entityManager.persist(shippingCompanyxRegion);
                entityManager.getTransaction().commit();
            }
        }
        TypedQuery<ShippingCompanyxRegion> query = entityManager.createQuery("SELECT s FROM ShippingCompanyxRegion s WHERE s.shippingCompanyName = :shippingCompanyName AND s.region = :region", ShippingCompanyxRegion.class);
        query.setParameter("shippingCompanyName", shippingCompanyName);
        query.setParameter("region", regionName);
        return query.getSingleResult();
    }

    public List<String> getGeographicalAreas(String shippingCompanyName) {
        TypedQuery<String> query = entityManager.createQuery("SELECT s.region FROM ShippingCompanyxRegion s WHERE s.shippingCompanyName = :shippingCompanyName", String.class);
        query.setParameter("shippingCompanyName", shippingCompanyName);
        return query.getResultList();
    }

    public boolean deleteShippingCompany(String shippingCompanyName) {
        ShippingCompany shippingCompany = getShippingCompanyByName(shippingCompanyName);
        if (shippingCompany != null) {
            entityManager.getTransaction().begin();
            entityManager.remove(shippingCompany);
            entityManager.getTransaction().commit();

            // Delete account using account service
            try {
                URL url = new URL(accountServiceUrl+"/delete/"+shippingCompany.getName());
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

            // remove from SellingCompanyxRegion
            TypedQuery<ShippingCompanyxRegion> query = entityManager.createQuery("SELECT s FROM ShippingCompanyxRegion s WHERE s.shippingCompanyName = :shippingCompanyName", ShippingCompanyxRegion.class);
            query.setParameter("shippingCompanyName", shippingCompanyName);
            List<ShippingCompanyxRegion> shippingCompanyxRegions = query.getResultList();
            for (ShippingCompanyxRegion shippingCompanyxRegion : shippingCompanyxRegions) {
                entityManager.getTransaction().begin();
                entityManager.remove(shippingCompanyxRegion);
                entityManager.getTransaction().commit();
            }

            return true;
        }
        return false;
    }

    public boolean removeRegionFromShippingCompany(String companyName , String regionName){
        TypedQuery<ShippingCompanyxRegion> query = entityManager.createQuery("SELECT s FROM ShippingCompanyxRegion s WHERE s.shippingCompanyName = :shippingCompanyName AND s.region = :region", ShippingCompanyxRegion.class);
        query.setParameter("shippingCompanyName", companyName);
        query.setParameter("region", regionName);
        List<ShippingCompanyxRegion> shippingCompanyxRegions = query.getResultList();
        if (shippingCompanyxRegions.size() > 0) {
            entityManager.getTransaction().begin();
            entityManager.remove(shippingCompanyxRegions.get(0));
            entityManager.getTransaction().commit();
            return true;
        }
        return false;
    }
}
