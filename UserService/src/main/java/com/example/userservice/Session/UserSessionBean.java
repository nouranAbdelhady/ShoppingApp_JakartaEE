package com.example.userservice.Session;

import com.example.userservice.User.Userr;

public interface UserSessionBean {
    public void login(Userr userr);
    public void logout();
    public Userr getLoggedInUser();
}
