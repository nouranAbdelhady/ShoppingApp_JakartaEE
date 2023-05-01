package com.example.accountservice.Session;

import com.example.accountservice.Account.Account;
import jakarta.ejb.Local;
import jakarta.ejb.Stateful;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.Context;

@Stateful
@Local(UserSessionBean.class)
public class UserSessionBean {
    private Account loggedInUser = null;

    public void login(Account userr) {
        this.loggedInUser = userr;
        System.out.println("Auth setting " + userr.getUsername());
    }

    public void logout() {
        this.loggedInUser = null;
    }

    public Account getLoggedInAccount() {
        return this.loggedInUser;
    }
}