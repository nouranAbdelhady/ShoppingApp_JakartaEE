package com.example.accountservice.Session;


import com.example.accountservice.Account.Account;

public interface UserSessionBean {
    public void login(Account account);
    public void logout();
    public Account getLoggedInAccount();
}
