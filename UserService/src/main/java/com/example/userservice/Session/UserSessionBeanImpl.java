package com.example.userservice.Session;

import com.example.userservice.User.Userr;
import jakarta.ejb.Local;
import jakarta.ejb.Stateful;

@Stateful
@Local(UserSessionBean.class)
public class UserSessionBeanImpl implements UserSessionBean {
    private static Userr loggedInUser = null;

    @Override
    public void login(Userr userr) {
        this.loggedInUser = userr;
        System.out.println("Auth setting " + userr.getUsername());
    }

    @Override
    public void logout() {
        this.loggedInUser = null;
    }

    @Override
    public Userr getLoggedInUser() {
        return this.loggedInUser;
    }
}