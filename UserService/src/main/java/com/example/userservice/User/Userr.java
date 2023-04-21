package com.example.userservice.User;

import com.example.userservice.GeographicalRegion.GeographicalRegion;
import jakarta.persistence.*;

@Entity
public class Userr {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String username;
    private String fullname;
    private String email;
    private String password;
    private boolean is_logged_in;

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
        this.is_logged_in = false;
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

    public boolean isIs_logged_in() {
        return is_logged_in;
    }

    public void setIs_logged_in(boolean is_logged_in) {
        this.is_logged_in = is_logged_in;
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

    public Boolean isAdmin() {
        return this.type.equals("Admin");
    }

    @Override
    public String toString() {
        return "Userr{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", is_logged_in=" + is_logged_in +
                ", type='" + type + '\'' +
                //", geographicalRegion=" + geographicalRegion +
                '}';
    }
}
