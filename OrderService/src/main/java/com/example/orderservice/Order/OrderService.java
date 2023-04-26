package com.example.orderservice.Order;

import jakarta.ejb.Stateless;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.persistence.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Stateless
public class OrderService {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("order");

    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    public List<Order> getAllOrders() {
        TypedQuery<Order> query = entityManager.createQuery("SELECT o FROM Order o", Order.class);
        List<Order> orders = query.getResultList();
        return orders;
    }

    public Order getOrderByID(int id) {
        return entityManager.find(Order.class, id);
    }

    public List<Order> getOrderByUsername(String username) {
        TypedQuery<Order> query = entityManager.createQuery("SELECT o FROM Order o WHERE o.username = :username", Order.class);
        query.setParameter("username", username);
        List<Order> orders = query.getResultList();
        return orders;
    }

    public void createOrder(Order order) {
        entityManager.getTransaction().begin();
        entityManager.persist(order);
        entityManager.getTransaction().commit();
    }

}

