package com.example.userservice.User;

import com.example.userservice.GeographicalRegion.GeographicalRegion;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Userr implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Unique username
    @Column(unique = true)
    private String username;
    private String fullname;
    private String email;
    private String password;
    private String type; // customer or admin

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "geographical_region_id")
    private GeographicalRegion geographicalRegion;

    public Userr() {
    }

    public Userr(String username, String fullname, String email, String password) {
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        //this.region = region;
    }

    public long getId() {
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public GeographicalRegion getGeographicalRegion() {
        return geographicalRegion;
    }

    public void setGeographicalRegion(GeographicalRegion geographicalRegion) {
        this.geographicalRegion = geographicalRegion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Userr{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", type='" + type + '\'' +
                //", geographicalRegion=" + geographicalRegion +
                '}';
    }
}
