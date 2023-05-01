package com.example.orderservice.Notification;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

@Stateless
public class NotificationService {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("order");

    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    public List<Notification> getAllNotifications() {
        return entityManager.createQuery("SELECT n FROM Notification n", Notification.class).getResultList();
    }

    public Notification getNotificationById(int id) {
        return entityManager.find(Notification.class, id);
    }

    public Notification addNotification(Notification notification) {
        System.out.println("addNotification method called");
        entityManager.getTransaction().begin();
        entityManager.persist(notification);
        entityManager.getTransaction().commit();
        System.out.println("Notification persisted: " + notification);
        return notification;
    }

    public List<Notification> getNotificationsByTargetedUsername(String username) {
        return entityManager.createQuery("SELECT n FROM Notification n WHERE n.targeted_username = :username", Notification.class).setParameter("username", username).getResultList();
    }

    public List<Notification> getNotificationsBySenderUsername(String username) {
        return entityManager.createQuery("SELECT n FROM Notification n WHERE n.sender_username = :username", Notification.class).setParameter("username", username).getResultList();
    }

    public void deleteNotification(int id) {
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.find(Notification.class, id));
        entityManager.getTransaction().commit();
    }

    public List<Notification> getSimilarRequests(String request, String date){
        return entityManager.createQuery("SELECT n FROM Notification n WHERE n.message = :request AND n.date = :date", Notification.class)
                .setParameter("request", request)
                .setParameter("date", date).getResultList();
    }

}
