package com.example.accountservice.Account;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Unique username
    @Column(unique = true)
    private String username;
    private String password;
    private String type;

    private boolean isLoggedIn;

    public Account() {
    }

    public Account(String username, String password, String type) {
        this.username = username;
        this.password = password;
        this.type = type;
        this.isLoggedIn = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}
