package com.example.accountservice.Account;

import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import java.util.List;

@Stateless
public class AccountService {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("account");

    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    public List<Account> getAllAccounts() {
        TypedQuery<Account> query = entityManager.createQuery("SELECT a FROM Account a", Account.class);
        List<Account> accounts = query.getResultList();
        return accounts;
    }

    public Account getAccountById(int id) {
        return entityManager.find(Account.class, id);
    }

    public void addAccount(Account account) {
        entityManager.getTransaction().begin();
        entityManager.persist(account);
        entityManager.getTransaction().commit();
    }

    public boolean updateAccount(String targeted,Account account) {
        Account targetedAccount = getByUsername(targeted);
        targetedAccount.setType(account.getType());
        targetedAccount.setUsername(account.getUsername());
        targetedAccount.setPassword(account.getPassword());
        entityManager.getTransaction().begin();
        entityManager.merge(targetedAccount);
        entityManager.getTransaction().commit();
        return true;
    }

    public boolean deleteAccount(String username) {
        Account targetedAccount = getByUsername(username);
        entityManager.getTransaction().begin();
        entityManager.remove(targetedAccount);
        entityManager.getTransaction().commit();
        return true;
    }

    public List<Account> getByType(String type) {
        TypedQuery<Account> query = entityManager.createQuery("SELECT a FROM Account a WHERE a.type =:type", Account.class);
        query.setParameter("type", type);
        List<Account> accounts = query.getResultList();
        return accounts;
    }

    public Account getByUsername(String username) {
        if (username == null) return null;
        TypedQuery<Account> query = entityManager.createQuery("SELECT a FROM Account a WHERE a.username =:username", Account.class);
        query.setParameter("username", username);
        Account account = query.getSingleResult();
        return account;
    }

    public Account login(Account account) {
        Account targetedAccount = getByUsername(account.getUsername());
        if (targetedAccount.getPassword().equals(account.getPassword())) {
            System.out.println("Login successful");
            targetedAccount.setLoggedIn(true);
            entityManager.getTransaction().begin();
            entityManager.merge(targetedAccount);
            entityManager.getTransaction().commit();
        }
        System.out.println("Login failed");
        return targetedAccount;
    }

    public Account logout(Account account) {
        Account targetedAccount = getByUsername(account.getUsername());
        targetedAccount.setLoggedIn(false);
        entityManager.getTransaction().begin();
        entityManager.merge(targetedAccount);
        entityManager.getTransaction().commit();
        return targetedAccount;
    }

    public Account getCredentials(String username) {
        Account targetedAccount = getByUsername(username);
        return targetedAccount;
    }


}
