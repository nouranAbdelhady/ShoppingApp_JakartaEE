package com.example.customerservice.Customer;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class CustomerService {
    @PersistenceContext(unitName = "customer")
    private EntityManager entityManager;

    @PostConstruct
    public void addDummyData() {
        // Add dummy data for testing
        this.addCustomer(new Customer("nouranahady","Nouran Abdelhady", "nouran@gmail.com","password"));
        this.addCustomer(new Customer("sarahalsisi","Sarah Alsisi", "sarah@gmail.com","password"));
        this.addCustomer(new Customer("sylviasami","Syvlia Sami", "sylvia@gmail.com","password"));
    }

    public List<Customer> getAllCustomers() {
        TypedQuery<Customer> query = entityManager.createQuery("SELECT c FROM Customer c", Customer.class);
        List<Customer> customers = query.getResultList();
        return customers;
    }

    public Customer getCustomertById(int id) {
        return entityManager.find(Customer.class, id);
    }

    public void addCustomer(Customer customer) {
        entityManager.persist(customer);
    }

}
