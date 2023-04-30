package com.example.orderservice.Order;

import com.example.orderservice.Notification.Notification;
import com.example.orderservice.Notification.NotificationService;
import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.transaction.Transactional;
import org.json.JSONObject;

import java.util.Date;

@Singleton
public class PurchaseService {

    private static PurchaseService instance = null;

    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("order");

    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    NotificationService notificationService;

    OrderService orderService;

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
            // Send a notification to the selling company
            int productId = newOrder.getProductId();
            JSONObject companyInfo = orderService.getCompanybyProductId(productId);
            String companyName = companyInfo.getString("name");

            Notification notification = new Notification();
            notification.setCustomerUsername(newOrder.getUsername());
            notification.setTargetedSellingCompany(companyName);
            notification.setMessage("A new order has been placed for your product with ID " + productId + ".");
            notification.setDate(String.valueOf(new Date()));
            notification.setRequest(true);
            notificationService.addNotification(notification);

            return newOrder;
        } catch (Exception e) {
            return null;
        }
    }
}
