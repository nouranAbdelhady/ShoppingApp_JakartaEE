package com.example.shippingservice.Shipping;

import jakarta.persistence.*;

@Entity
@Table(name = "company_regions")
public class ShippingCompanyxRegion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String shippingCompanyName;
    private String region;

    public ShippingCompanyxRegion() {
    }

    public ShippingCompanyxRegion(String shippingCompanyName, String region) {
        this.shippingCompanyName = shippingCompanyName;
        this.region = region;
    }

    public String getShippingCompanyName() {
        return shippingCompanyName;
    }

    public void setShippingCompanyName(String shippingCompanyName) {
        this.shippingCompanyName = shippingCompanyName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
