package com.example.orderservice.Order;

import java.io.Serializable;
import jakarta.persistence.*;

@Entity
@Table(name = "`order`")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private int productId;
    private double amount;
    private String state;
    private String shipping_company;

    public Order() {}

    public Order(String username, int productID, double amount, String state)
    {
        this.username=username;
        this.productId=productID;
        this.amount=amount;
        this.state=state;
        this.shipping_company="";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getProductId() {
        return productId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getShipping_company() {
        return shipping_company;
    }

    public void setShipping_company(String shipping_company) {
        this.shipping_company = shipping_company;
    }
}
