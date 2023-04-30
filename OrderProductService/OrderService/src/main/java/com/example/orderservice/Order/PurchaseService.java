package com.example.orderservice.Order;

import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.transaction.Transactional;

@Singleton
public class PurchaseService {
    private static PurchaseService instance = null;

    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("order");

    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    public static PurchaseService getInstance() {
        if (instance == null) {
            instance = new PurchaseService();
            System.out.println("Singleton: instance created");
        }
        System.out.println("Singleton: instance already created");
        return instance;
    }

    @Transactional
    public Order addPurchase(Order newOrder) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(newOrder);
            entityManager.getTransaction().commit();
            return newOrder;
        } catch (Exception e) {
            return null;
        }
    }
}
