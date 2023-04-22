package com.example.userservice.User;

import com.example.userservice.GeographicalRegion.GeographicalRegion;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;

import java.util.List;

@Stateless
public class UserService {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("userr");

    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

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
        return entityManager.find(Userr.class, id);
    }

    public void addUser(Userr userr) {
        entityManager.getTransaction().begin();
        entityManager.persist(userr);
        entityManager.getTransaction().commit();
    }

    public boolean updateUser(Userr userr) {
        Userr targetedUserr = getUserById((int) userr.getId());
        targetedUserr.setGeographicalRegion(userr.getGeographicalRegion());
        targetedUserr.setType(userr.getType());
        targetedUserr.setUsername(userr.getUsername());
        targetedUserr.setEmail(userr.getEmail());
        targetedUserr.setPassword(userr.getPassword());
        targetedUserr.setFullname(userr.getFullname());
        entityManager.getTransaction().begin();
        Userr newUserr = entityManager.merge(targetedUserr);
        entityManager.getTransaction().commit();

        // Check if merge is successful
        return newUserr != null;
    }

    public boolean deleteUser(Userr userr) {
        entityManager.getTransaction().begin();
        entityManager.remove(userr);
        entityManager.getTransaction().commit();
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
