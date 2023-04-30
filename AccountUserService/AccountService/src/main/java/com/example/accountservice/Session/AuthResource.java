package com.example.accountservice.Session;

import com.example.accountservice.Account.Account;
import com.example.accountservice.Account.AccountService;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
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
    public Account login(Account user, @Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("username", user.getUsername());

        // Perform login authentication and authorization checks
        Account loggedInUser = accountService.login(user);
        if(loggedInUser!=null){
            // If login is successful, set the logged-in user in the session bean
            System.out.println("User logged in: " + loggedInUser.getUsername());
            userSessionBean.login(loggedInUser);
            return loggedInUser;
        }
        else{
            // If login is unsuccessful
            return null;
        }
    }

    @PUT
    @Path("/logout")
    public Account logout(@Context HttpServletRequest request) {
        // Get the logged-in user from the session bean and clear the session
        HttpSession session = request.getSession(false); // Do not create a new session if it does not exist
        Account loggedInUser=null;
        loggedInUser = userSessionBean.getLoggedInAccount();
        if (session != null) {
            //loggedInUser = accountService.getByUsername((String) session.getAttribute("username"));
            session.invalidate();

        }
        // Reset the logged-in user in the session bean
        userSessionBean.logout();
        accountService.logout(loggedInUser);
        return loggedInUser;
    }

    @GET
    @Path("/user")
    public Account getLoggedInUser(@Context HttpServletRequest request) {
        // Get the logged-in user from the session bean
        HttpSession session = request.getSession();
        System.out.println("Session: " + session.getAttribute("username"));
        //Account loggedInUser = accountService.getByUsername((String) session.getAttribute("username"));
        Account loggedInUser = userSessionBean.getLoggedInAccount();
        if (loggedInUser == null) {
            return null;
        }
        return loggedInUser;
    }
}
