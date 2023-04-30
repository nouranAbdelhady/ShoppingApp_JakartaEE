package com.example.userservice.User.Admin;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/admin")     //http://localhost:16957/UserService-1.0-SNAPSHOT/api/admin
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminResource {

    @EJB
    AdminService adminService;

    @POST
    @Path("/add_selling_company")
    @Consumes(MediaType.TEXT_PLAIN)
    public boolean addSellingCompany(String name) {
        return adminService.getInstance().addSellingCompany(name);
    }

    @POST
    @Path("/add_shipping_company")
    @Consumes(MediaType.TEXT_PLAIN)
    public boolean addShippingCompany(String body) {
        // Split by comma
        String[] credentials = body.split(",");
        String username = credentials[0];
        String password = credentials[1];
        return adminService.getInstance().addShippingCompany(username, password);
    }

    @GET
    @Path("/get_credentials/{name}")
    public List<String> getCredentials(@PathParam("name") String name) {
        return adminService.getInstance().getCredentials(name);
    }

    @GET
    @Path("/selling_company/unassigned_names")
    public List<String> getUnassignedSellingCompanyNames() {
        return adminService.getInstance().getUnassignedRepresentativeNames();
    }


}
