package com.example.shippingservice.Notification;

import com.example.shippingservice.MDB.SendService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/notifactions")     //http://localhost:8080/ShippingService-1.0-SNAPSHOT/api/notifactions
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NotificationResource {
    @EJB
    NotificationService notificationService;

    @EJB
    SendService sendService;

    @GET
    public List<Notification> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @GET
    @Path("/{id}")
    public Notification getNotificationById(@PathParam("id") int id) {
        return notificationService.getNotificationById(id);
    }

    @GET
    @Path("/receiver/{username}")
    public List<Notification> getNotificationsByTargetedUsername(@PathParam("username") String username) {
        return notificationService.getNotificationsByTargetedUsername(username);
    }

    @GET
    @Path("/sender/{username}")
    public List<Notification> getNotificationsBySenderUsername(@PathParam("username") String username) {
        return notificationService.getNotificationsBySenderUsername(username);
    }

    @POST
    public Notification addNotification(Notification notification) {
        return notificationService.addNotification(notification);
    }

    @DELETE
    @Path("/{id}")
    public void deleteNotification(@PathParam("id") int id) {
        notificationService.deleteNotification(id);
    }

    @GET
    @Path("/requests")
    @Consumes("text/plain")
    public List<Notification> getSimilarRequests(String body) {
        String[] parts = body.split(";");
        String message = parts[0];
        String date = parts[1];
        return notificationService.getSimilarRequests(message, date);
    }


}
