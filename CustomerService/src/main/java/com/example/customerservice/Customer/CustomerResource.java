package com.example.customerservice.Customer;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/customers")     //http://localhost:8080/CustomerService-1.0-SNAPSHOT/api/customers
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerResource {

    @EJB
    private CustomerService customerService;

    @GET
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GET
    @Path("/{CustomerId}")
    public Customer getCustomerById(@PathParam("CustomerId") int id) {
        return customerService.getCustomertById(id);
    }

    @POST
    public Customer addCustomer(Customer customer) {
        customerService.addCustomer(customer);
        return customer;
    }

    @PUT
    @Path("/{CustomerId}")
    public boolean updateCustomer(@PathParam("CustomerId") int id, Customer customer) {
        customer.setId(id);
        return customerService.updateCustomer(customer);
    }

    @DELETE
    @Path("/{CustomerId}")
    public boolean deleteCustomer(@PathParam("CustomerId") int id) {
        return customerService.deleteCustomer(customerService.getCustomertById(id));
    }

    @PUT
    @Path("/login")
    public boolean login(Customer customer) {
        return customerService.login(customer);
    }

    @PUT
    @Path("/logout")
    public boolean logout(Customer customer) {
        return customerService.logout(customer.getUsername());
    }
}
