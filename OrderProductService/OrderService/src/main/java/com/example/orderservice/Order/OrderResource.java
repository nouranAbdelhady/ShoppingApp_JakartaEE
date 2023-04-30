package com.example.orderservice.Order;

import jakarta.ejb.EJB;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/orders")  //http://localhost:9314/OrderService-1.0-SNAPSHOT/api/orders
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {

    @EJB
    private OrderService orderService;

    @GET
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GET
    @Path("/{OrderId}")
    public Order getOrderById(@PathParam("OrderId") int id) {
        return orderService.getOrderByID(id);
    }

    @GET
    @Path("getByName/{Username}")
    public List<Order> getOrderByUsername(@PathParam("Username") String username) {
        return orderService.getOrderByUsername(username);
    }


    @DELETE
    @Path("/delete/{OrderId}")
    public boolean deleteAccount(@PathParam("OrderId") int Id) {
        return orderService.deleteOrder(Id);
    }

    @PUT
    @Path("/update/{OrderId}")
    public Order updateOrder(@PathParam("OrderId") int id, Order order) {
        order.setId(id);
        orderService.updateOrder(order);
        return order;
    }

    @GET
    @Path("/state/{state}")
    public List<Order> getOrderByState(@PathParam("state") String state) {
        return orderService.getOrderByState(state);
    }

    @GET
    @Path("/nameAndState/{username}/{state}")
    public List<Order> getOrderByNameAndState(@PathParam("username") String username, @PathParam("state") String state) {
        return orderService.getOrderByNameAndState(username,state);
    }

    @GET
    @Path("/productId/{productId}")
    public Order getOrdersByProductId(@PathParam("productId") int productId) {
        return orderService.getOrderByProductId(productId);
    }

    @GET
    @Path("/getCustomerDetailsbyUsername/{username}")
    public List<List<String>> getCustomerDetailsbyUsername(@PathParam("username") String username) {
        return orderService.getCustomerDetailsbyUsername(username);
    }

    @PUT
    @Path("/updateState/{OrderId}")
    @Consumes(MediaType.TEXT_PLAIN)
    public String updateOrderState(@PathParam("OrderId") int id, String state) {
        return orderService.updateOrderState(id,state);
    }

    @PUT
    @Path("/updateShippingCompany/{OrderId}")
    @Consumes(MediaType.TEXT_PLAIN)
    public String updateShippingCompany(@PathParam("OrderId") int id, String shippingCompany) {
        return orderService.updateShippingCompany(id,shippingCompany);
    }

}
