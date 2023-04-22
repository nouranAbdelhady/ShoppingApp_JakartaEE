package com.example.productservice.Product;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import java.util.List;

@Stateless
public class ProductService {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("product");

    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    public List<Product> getAllProducts() {
        TypedQuery<Product> query = entityManager.createQuery("SELECT p FROM Product p", Product.class);
        List<Product> products = query.getResultList();
        return products;
    }

    public Product getProductById(int id) {
        return entityManager.find(Product.class, id);
    }

    public void addProduct(Product product) {
        entityManager.getTransaction().begin();
        entityManager.persist(product);
        entityManager.getTransaction().commit();
    }

    public void updateProduct(Product product) {
        entityManager.getTransaction().begin();
        entityManager.merge(product);
        entityManager.getTransaction().commit();
    }
    // get products based on state
    public List<Product> getProductsByState(String state) {
        TypedQuery<Product> query = entityManager.createQuery("SELECT p FROM Product p WHERE p.state = :state", Product.class);
        query.setParameter("state", state);
        List<Product> products = query.getResultList();
        return products;
    }

}
