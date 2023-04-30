package com.example.productservice.SellingCompany;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "representative_name")
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
