package com.example.accountservice.Account;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/accounts")     //http://localhost:8080/AccountService-1.0-SNAPSHOT/api/account
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {

    @EJB
    private AccountService accountService;

    @GET
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GET
    @Path("/type/{type}")
    public List<Account> getByType(@PathParam("type") String type) {
        return accountService.getByType(type);
    }

    @GET
    @Path("/{AccountId}")
    public Account getAccountById(@PathParam("AccountId") int id) {
        return accountService.getAccountById(id);
    }

    @POST
    public Account addAccount(Account account) {
        accountService.addAccount(account);
        return account;
    }

    @PUT
    @Path("/{AccountName}")
    public boolean updateAccount(@PathParam("AccountName") String name, Account account) {
        return accountService.updateAccount(name,account);
    }

    @DELETE
    @Path("/delete/{AccountName}")
    public boolean deleteAccount(@PathParam("AccountName") String name) {
        return accountService.deleteAccount(name);
    }

    @PUT
    @Path("/login")
    public Account login(Account account) {
        return accountService.login(account);
    }

    @PUT
    @Path("/logout")
    public Account logout(Account account) {
        return accountService.logout(account);
    }


}
