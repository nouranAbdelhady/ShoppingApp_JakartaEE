package com.example.userservice.User;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/users")     //http://localhost:8080/UserService-1.0-SNAPSHOT/api/users
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @EJB
    private UserService userService;

    @GET
    public List<Userr> getAllUsers() {
        return userService.getAllUsers();
    }

    @GET
    @Path("/customers")
    public List<Userr> getAllCustomers() {
        return userService.getAllCustomers();
    }

    @GET
    @Path("admins")
    public List<Userr> getAllAdmins() {
        return userService.getAllAdmins();
    }

    @GET
    @Path("/{UserId}")
    public Userr getUserById(@PathParam("UserId") int id) {
        return userService.getUserById(id);
    }

    @POST
    public Userr addUser(Userr userr) {
        userService.addUser(userr);
        return userr;
    }

    @PUT
    @Path("/{UserId}")
    public boolean updateUser(@PathParam("UserId") int id, Userr userr) {
        userr.setId(id);
        return userService.updateUser(userr);
    }

    @DELETE
    @Path("/{UserId}")
    public boolean deleteUser(@PathParam("UserId") int id) {
        return userService.deleteUser(userService.getUserById(id));
    }

    @PUT
    @Path("/login")
    public Userr login(Userr userr) {
        return userService.login(userr);
    }

    @PUT
    @Path("/logout")
    public Userr logout(Userr userr) {
        return userService.logout(userr.getUsername());
    }
}
