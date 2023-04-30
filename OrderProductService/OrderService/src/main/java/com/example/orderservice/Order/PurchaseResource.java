package com.example.orderservice.Order;

import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/purchase")          // http://localhost:9314/OrderService-1.0-SNAPSHOT/api/purchase
@Consumes("application/json")
@Produces("application/json")
public class PurchaseResource {
    @EJB
    private PurchaseService purchaseService;

    @POST
    public Order addPurchase(Order newOrder) {      // Add a new order
        return purchaseService.getInstance().addPurchase(newOrder);
    }
}
