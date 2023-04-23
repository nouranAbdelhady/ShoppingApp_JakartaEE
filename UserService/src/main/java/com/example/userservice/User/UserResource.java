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
    @Path("/{Username}")
    public boolean updateUser(@PathParam("Username") String name, Userr userr) {
        return userService.updateUser(name, userr);
    }

    @DELETE
    @Path("/{Username}")
    public boolean deleteUser(@PathParam("Username") String username) {
        return userService.deleteUser(userService.getUserByUsername(username));
    }

    @GET
    @Path("/username/{Username}")
    public Userr getUserByUsername(@PathParam("Username") String username) {
        return userService.getUserByUsername(username);
    }
}
