package com.example.userservice.User.Admin;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/admin")     //http://localhost:8080/UserService-1.0-SNAPSHOT/api/admin
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminResource {

    @EJB
    AdminService adminService;

    @POST
    @Path("/add_selling_company")
    @Consumes(MediaType.TEXT_PLAIN)
    public boolean addSellingCompany(String name) {
        return adminService.addSellingCompany(name);
    }

    @GET
    @Path("/get_selling_company_credentials/{name}")
    public List<String> getSellingCompanyCredentials(@PathParam("name") String name) {
        return adminService.getSellingCompanyCredentials(name);
    }

    @GET
    @Path("/selling_company/unassigned_names")
    public List<String> getUnassignedSellingCompanyNames() {
        return adminService.getUnassignedRepresentativeNames();
    }


}
