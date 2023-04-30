package com.example.productservice.Product;

import java.io.Serializable;

import com.example.productservice.SellingCompany.SellingCompany;
import jakarta.persistence.*;

@Entity
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private double price;
    private String state;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "selling_company_id")
    private SellingCompany sellingCompany;

    public Product(){
        this.state="available";
    }

    public Product(String name, String description, double price, SellingCompany sellingCompany) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.state = "Available";
        this.sellingCompany = sellingCompany;
    }

    public Product(String name, String description, double price, String imageUrl, SellingCompany sellingCompany) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.state = "Available";
        this.imageUrl = imageUrl;
        this.sellingCompany = sellingCompany;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setSellingCompany(SellingCompany sellingCompany) {
        this.sellingCompany = sellingCompany;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", state='" + state + '\'' +
                ", sellingCompany=" + sellingCompany +
                '}';
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}