package com.example.userservice.Session;

import com.example.userservice.User.UserService;
import com.example.userservice.User.Userr;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @EJB
    private UserSessionBean userSessionBean;

    @EJB
    private UserService userService;

    @PUT
    @Path("/login")
    public Userr login(Userr user) {
        // Perform login authentication and authorization checks
        Userr loggedInUser = userService.login(user);
        if(loggedInUser!=null){
            // If login is successful, set the logged-in user in the session bean
            System.out.println("User logged in: " + loggedInUser.getUsername());
            userSessionBean.login(loggedInUser);
            System.out.println("Test " + userSessionBean.getLoggedInUser().getUsername());
            return loggedInUser;
        }
        else{
            // If login is unsuccessful, return an unauthorized response
            return null;
        }
    }

    @PUT
    @Path("/logout")
    public Userr logout() {
        // Get the logged-in user from the session bean and clear the session
        Userr loggedInUser = userSessionBean.getLoggedInUser();
        if (loggedInUser == null) {
            return null;
        }
        userSessionBean.logout();
        userService.logout(loggedInUser.getUsername());
        return loggedInUser;
    }

    @GET
    @Path("/user")
    public Userr getLoggedInUser() {
        // Get the logged-in user from the session bean
        Userr loggedInUser = userSessionBean.getLoggedInUser();
        if (loggedInUser == null) {
            return null;
        }
        return loggedInUser;
    }
}
