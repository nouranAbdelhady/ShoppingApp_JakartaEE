package com.example.orderservice.Notification;

import jakarta.persistence.*;

@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String targeted_username;
    private String message;

    private String sender_username;

    private String date;
    private boolean request;

    public Notification() {
    }

    public Notification(String targeted_username, String message, String sender_username, boolean request) {
        this.sender_username = sender_username;
        this.message = message;
        this.targeted_username = targeted_username;
        this.date = java.time.LocalDate.now().toString();
        this.request = request;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender_username() {
        return sender_username;
    }

    public void setCustomerUsername(String sender_username) {
        this.sender_username = sender_username;
    }

    public String getTargeted_username() {
        return targeted_username;
    }

    public void setTargetedSellingCompany(String targeted_username) {
        this.targeted_username = targeted_username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isRequest() {
        return request;
    }

    public void setRequest(boolean request) {
        this.request = request;
    }

}
