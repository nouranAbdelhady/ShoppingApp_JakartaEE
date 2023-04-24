package com.example.productservice.SellingCompany;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class RepresentativeName {
    @Id
    @Column(name = "name", nullable = false)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RepresentativeName() {
    }

    public RepresentativeName(String name) {
        this.name = name;
    }
}
