package com.example.shippingservice.Shipping;

import jakarta.persistence.*;

@Entity
@Table(name = "shipping_company")
public class ShippingCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    // unique
    @Column(unique = true)
    private String name;
    private String password;

    public ShippingCompany() {

    }
    public ShippingCompany(String companyName, String password) {
        this.name = companyName;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
