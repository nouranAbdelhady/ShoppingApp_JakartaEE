package com.example.userservice.GeographicalRegion;

import com.example.userservice.User.Userr;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class GeographicalRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    String name;

    @OneToMany(mappedBy = "geographicalRegion", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Userr> userrs;

    public GeographicalRegion() {
    }

    public GeographicalRegion(String name) {
        this.name = name;
    }

    public long getId() {
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

    public void setUsers(List<Userr> userrs) {
        this.userrs = userrs;
    }

}
