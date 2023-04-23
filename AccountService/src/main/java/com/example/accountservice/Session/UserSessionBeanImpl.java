package com.example.accountservice.Session;

import com.example.accountservice.Account.Account;
import jakarta.ejb.Local;
import jakarta.ejb.Stateful;

@Stateful
@Local(UserSessionBean.class)
public class UserSessionBeanImpl implements UserSessionBean {
    private static Account loggedInUser = null;

    @Override
    public void login(Account userr) {
        this.loggedInUser = userr;
        System.out.println("Auth setting " + userr.getUsername());
    }

    @Override
    public void logout() {
        this.loggedInUser = null;
    }

    @Override
    public Account getLoggedInAccount() {
        return this.loggedInUser;
    }
}