package com.example.orderservice.Order;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/orders")  //http://localhost:8080/OrderService-1.0-SNAPSHOT/api/orders
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {

    @EJB
    private OrderService orderService;

    @GET
    public List<Order> getAllOrders() {return orderService.getAllOrders();}

    @GET
    @Path("/{OrderId}")
    public Order getOrderById(@PathParam("OrderId") int id) {return orderService.getOrderByID(id);}

    @POST
    public Order createOrder(Order order)
    {
        orderService.createOrder(order);
        return order;
    }

}
