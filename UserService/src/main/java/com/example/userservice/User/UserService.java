package com.example.userservice.User;

import com.example.userservice.GeographicalRegion.GeographicalRegion;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class UserService {
    @PersistenceContext(unitName = "userr")
    private EntityManager entityManager;

    @PostConstruct
    public void addDummyData() {
        // Add dummy data for testing

        // Customers
        Userr userr1 = new Userr("nouranahady","Nouran Abdelhady", "nouran@gmail.com","password");
        Userr userr2 = new Userr("sarahalsisi","Sarah Alsisi", "sarah@gmail.com","password");
        Userr userr3 = new Userr("sylviasami","Syvlia Sami", "sylvia@gmail.com","password");

        // Add regions to customers
        GeographicalRegion region1 = new GeographicalRegion("Maadi");
        entityManager.persist(region1);
        userr1.setGeographicalRegion(region1);
        userr1.setType("Customer");

        GeographicalRegion region2 = new GeographicalRegion("Fifth Settlement");
        entityManager.persist(region2);
        userr2.setGeographicalRegion(region2);
        userr2.setType("Customer");

        GeographicalRegion region3 = new GeographicalRegion("Dokki");
        entityManager.persist(region3);
        //userr3.setGeographicalRegion(region3);
        userr3.setType("Customer");
        entityManager.persist(userr1);
        entityManager.persist(userr2);
        entityManager.persist(userr3);

        // Admins
        Userr admin1 = new Userr("admin1","Admin 1", "admin1@gmail.com","password");
        Userr admin2 = new Userr("admin2","Admin 2", "admin2@gmail.com","password");
        admin1.setType("Admin");
        admin2.setType("Admin");
        entityManager.persist(admin1);
        entityManager.persist(admin2);
    }

    public List<Userr> getAllUsers() {
        TypedQuery<Userr> query = entityManager.createQuery("SELECT c FROM Userr c", Userr.class);
        List<Userr> userrs = query.getResultList();
        return userrs;
    }

    public List<Userr> getAllCustomers() {
        TypedQuery<Userr> query = entityManager.createQuery("SELECT c FROM Userr c WHERE c.type ='Customer'", Userr.class);
        List<Userr> userrs = query.getResultList();
        return userrs;
    }

    public List<Userr> getAllAdmins() {
        TypedQuery<Userr> query = entityManager.createQuery("SELECT c FROM Userr c WHERE c.type ='Admin'", Userr.class);
        List<Userr> userrs = query.getResultList();
        return userrs;
    }

    public Userr getUserById(int id) {
        return entityManager.find(Userr.class, (long) id);
    }

    public void addUser(Userr userr) {
        entityManager.persist(userr);
    }

    public boolean updateUser(Userr userr) {
        Userr targetedUserr = getUserById((int) userr.getId());
        targetedUserr.setGeographicalRegion(userr.getGeographicalRegion());
        targetedUserr.setType(userr.getType());
        targetedUserr.setUsername(userr.getUsername());
        targetedUserr.setEmail(userr.getEmail());
        targetedUserr.setPassword(userr.getPassword());
        targetedUserr.setFullname(userr.getFullname());
        Userr newUserr = entityManager.merge(targetedUserr);

        // Check if merge is successful
        return newUserr != null;
    }

    public boolean deleteUser(Userr userr) {
        entityManager.remove(userr);
        // Check if remove is successful
        if (entityManager.find(Userr.class, userr.getId()) == null) {
            return true;
        }
        return false;
    }

    public Userr getUserByUsername(String username) {
        TypedQuery<Userr> query = entityManager.createQuery("SELECT c FROM Userr c WHERE c.username = :username", Userr.class);
        query.setParameter("username", username);
        return query.getSingleResult();
    }

    public boolean login(Userr userr) {
        String username = userr.getUsername();
        String password = userr.getPassword();
        Userr targetedUserr = getUserByUsername(username);
        if (targetedUserr.getPassword().equals(password)) {
            targetedUserr.setIs_logged_in(true);
            updateUser(targetedUserr);
            return true;
        }
        return false;
    }

    public boolean logout(String username) {
        Userr targetedUserr = getUserByUsername(username);
        if (targetedUserr.isIs_logged_in()) {
            targetedUserr.setIs_logged_in(false);
            updateUser(targetedUserr);
            return true;
        }
        return false;
    }

}
