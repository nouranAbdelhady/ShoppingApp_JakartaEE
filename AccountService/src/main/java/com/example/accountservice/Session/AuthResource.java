package com.example.accountservice.Session;

import com.example.accountservice.Account.Account;
import com.example.accountservice.Account.AccountService;
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
    private AccountService accountService;

    @PUT
    @Path("/login")
    public Account login(Account user) {
        // Perform login authentication and authorization checks
        Account loggedInUser = accountService.login(user);
        if(loggedInUser!=null){
            // If login is successful, set the logged-in user in the session bean
            System.out.println("User logged in: " + loggedInUser.getUsername());
            userSessionBean.login(loggedInUser);
            System.out.println("Test " + userSessionBean.getLoggedInAccount().getUsername());
            return loggedInUser;
        }
        else{
            // If login is unsuccessful
            return null;
        }
    }

    @PUT
    @Path("/logout")
    public Account logout() {
        // Get the logged-in user from the session bean and clear the session
        Account loggedInUser = userSessionBean.getLoggedInAccount();
        if (loggedInUser == null) {
            return null;
        }
        userSessionBean.logout();
        accountService.logout(loggedInUser);
        return loggedInUser;
    }

    @GET
    @Path("/user")
    public Account getLoggedInUser() {
        // Get the logged-in user from the session bean
        Account loggedInUser = userSessionBean.getLoggedInAccount();
        if (loggedInUser == null) {
            return null;
        }
        return loggedInUser;
    }
}
