package com.example.userservice.User.Admin;

import com.google.gson.JsonArray;
import jakarta.ejb.Singleton;
import jakarta.json.Json;
import jakarta.json.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
@Singleton
public class AdminService {

    private String accountServiceUrl = "http://localhost:16957/AccountService-1.0-SNAPSHOT/api/accounts";
    private String sellingCompanyServiceUrl = "http://localhost:9314/ProductService-1.0-SNAPSHOT/api/selling_company";
    private String shippingCompanyServiceUrl = "http://localhost:8080/ShippingService-1.0-SNAPSHOT/api/shipping_company";

    private static AdminService instance = null;

    public static AdminService getInstance() {
        if (instance == null) {
            instance = new AdminService();
            System.out.println("Singleton: instance created");
        }
        System.out.println("Singleton: instance already created");
        return instance;
    }

    public boolean addSellingCompany(String name) {
        System.out.println("You entered: " + name);
        JsonObject newSellingCompany = Json.createObjectBuilder()
                .add("name", name)
                .build();
        // Send request to account service
        try {
            URL url = new URL(sellingCompanyServiceUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            // Write the Selling Company object to the request body
            String companyJson = newSellingCompany.toString();
            conn.getOutputStream().write(companyJson.getBytes());
            // Get the response code and response message
            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();
            System.out.println("Response code: " + responseCode);
            System.out.println("Response message: " + responseMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addShippingCompany(String name, String password) {
        System.out.println("You entered: " + name + " " + password);
        JsonObject newShippingCompany = Json.createObjectBuilder()
                .add("name", name)
                .add("password", password)
                .build();
        // Send request to account service
        try {
            URL url = new URL(shippingCompanyServiceUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            // Write the Shipping Company object to the request body
            String companyJson = newShippingCompany.toString();
            conn.getOutputStream().write(companyJson.getBytes());
            // Get the response code and response message
            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();
            System.out.println("Response code: " + responseCode);
            System.out.println("Response message: " + responseMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<String> getUnassignedRepresentativeNames() {
        try {
            URL url = new URL(sellingCompanyServiceUrl + "/representative_name/unassigned");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
                System.out.println("Output: "+output);
            }

            conn.disconnect();

            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(responseBuilder.toString(), JsonArray.class);
            System.out.println("JsonArray: "+jsonArray.toString());

            List<String> names = new ArrayList<>();
            for (com.google.gson.JsonElement jsonElement : jsonArray) {
                System.out.println("JsonElement: "+jsonElement.toString());
                com.google.gson.JsonObject jsonObject = jsonElement.getAsJsonObject();
                System.out.println("JsonObject: "+jsonObject.toString());
                String name = jsonObject.get("name").getAsString();
                names.add(name);
                System.out.println("Name: "+name);
            }

            return names;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getCredentials(String targetedName){
        try {
            URL url = new URL(accountServiceUrl + "/credentials/"+targetedName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
                System.out.println("Output: "+output);
            }
            conn.disconnect();

            // Split the response into the username and password when the response finds "name:" and "password:" respectively
            String response = responseBuilder.toString();
            String[] responseArray = response.split(",");

            String username = null, password =null;
            for (String s : responseArray) {
                System.out.println("ResponseArray: "+s);
                if (s.contains("username")) {
                    // split at the "name:" to get the username
                    s = s.split(":")[1];
                    // remove "" from the username
                    s = s.substring(1, s.length()-1);
                    System.out.println("Username: "+s);
                    username = s;
                }
                if (s.contains("password")) {
                    // split at the "password:" to get the password
                    s = s.split(":")[1];
                    // remove "" from the password
                    s = s.substring(1, s.length()-1);
                    password = s;
                }
            }

            List<String> credentials = new ArrayList<>();
            credentials.add(username);
            credentials.add(password);
            return credentials;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }





}
