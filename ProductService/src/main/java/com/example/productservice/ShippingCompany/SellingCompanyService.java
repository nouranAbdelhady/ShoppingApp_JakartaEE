package com.example.productservice.ShippingCompany;

import com.example.productservice.Product.Product;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.persistence.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Stateless
public class SellingCompanyService {

    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("product");

    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    private String accountServiceUrl= "http://localhost:8080/AccountService-1.0-SNAPSHOT/api/accounts";

    public void addSellingCompany(SellingCompany sellingCompany) {
        entityManager.getTransaction().begin();
        entityManager.persist(sellingCompany);
        entityManager.getTransaction().commit();;

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

    public void updateSellingCompany(String targetedName,SellingCompany sellingCompany) {
        entityManager.getTransaction().begin();
        entityManager.merge(sellingCompany);
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
            URL url = new URL(accountServiceUrl+"/"+targetedName);
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
    }

    public void deleteSellingCompany(SellingCompany sellingCompany) {
        entityManager.getTransaction().begin();
        entityManager.remove(sellingCompany);
        entityManager.getTransaction().commit();

        // Delete account using account service
        try {
            URL url = new URL(accountServiceUrl+"/delete/"+sellingCompany.getName());
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
        entityManager.getTransaction().begin();
        entityManager.merge(sellingCompany);
        entityManager.getTransaction().commit();
    }

    public List<Product> getProductsBySellingCompany(int sellingCompanyId) {
        return entityManager.createQuery("SELECT p FROM Product p WHERE p.sellingCompany.id = :sellingCompanyId", Product.class)
                .setParameter("sellingCompanyId", sellingCompanyId)
                .getResultList();
    }

    public boolean deleteProductFromSellingCompany(int sellingCompanyId, int productId) {
        SellingCompany sellingCompany = getSellingCompanyById(sellingCompanyId);
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

}
