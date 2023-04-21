package com.example.customerservice.Customer;

import com.example.customerservice.GeographicalRegion.GeographicalRegion;
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
        Customer customer1 = new Customer("nouranahady","Nouran Abdelhady", "nouran@gmail.com","password");
        Customer customer2 = new Customer("sarahalsisi","Sarah Alsisi", "sarah@gmail.com","password");
        Customer customer3 = new Customer("sylviasami","Syvlia Sami", "sylvia@gmail.com","password");

        // Add regions to customers
        GeographicalRegion region1 = new GeographicalRegion("Maadi");
        entityManager.persist(region1);
        customer1.setGeographicalRegion(region1);

        GeographicalRegion region2 = new GeographicalRegion("Fifth Settlement");
        entityManager.persist(region2);
        customer2.setGeographicalRegion(region2);

        GeographicalRegion region3 = new GeographicalRegion("Dokki");
        entityManager.persist(region3);
        //customer3.setGeographicalRegion(region3);
        entityManager.persist(customer1);
        entityManager.persist(customer2);
        entityManager.persist(customer3);
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

    public boolean updateCustomer(Customer customer) {
        Customer targetedCustomer = getCustomertById(customer.getId());
        targetedCustomer.setGeographicalRegion(customer.getGeographicalRegion());
        targetedCustomer.setUsername(customer.getUsername());
        targetedCustomer.setEmail(customer.getEmail());
        targetedCustomer.setPassword(customer.getPassword());
        targetedCustomer.setFullname(customer.getFullname());
        Customer newCustomer = entityManager.merge(targetedCustomer);

        // Check if merge is successful
        return newCustomer != null;
    }

    public boolean deleteCustomer(Customer customer) {
        entityManager.remove(customer);
        // Check if remove is successful
        if (entityManager.find(Customer.class, customer.getId()) == null) {
            return true;
        }
        return false;
    }

    public Customer getCustomerByUsername(String username) {
        TypedQuery<Customer> query = entityManager.createQuery("SELECT c FROM Customer c WHERE c.username = :username", Customer.class);
        query.setParameter("username", username);
        return query.getSingleResult();
    }

    public boolean login(Customer customer) {
        String username = customer.getUsername();
        String password = customer.getPassword();
        Customer targetedCustomer = getCustomerByUsername(username);
        if (targetedCustomer.getPassword().equals(password)) {
            targetedCustomer.setIs_logged_in(true);
            updateCustomer(targetedCustomer);
            return true;
        }
        return false;
    }

    public boolean logout(String username) {
        Customer targetedCustomer = getCustomerByUsername(username);
        if (targetedCustomer.isIs_logged_in()) {
            targetedCustomer.setIs_logged_in(false);
            updateCustomer(targetedCustomer);
            return true;
        }
        return false;
    }

}
