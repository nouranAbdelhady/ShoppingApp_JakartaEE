package com.example.customerservice.GeographicalRegion;

import com.example.customerservice.Customer.Customer;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class GeographicalRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    String name;

    @OneToMany(mappedBy = "geographicalRegion", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Customer> customers;

    public GeographicalRegion() {
    }

    public GeographicalRegion(String name) {
        this.name = name;
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

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

}
