package com.example.productservice.ShippingCompany;

import com.example.productservice.Product.Product;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class SellingCompanyService {
    @PersistenceContext(unitName = "product")
    private EntityManager entityManager;

    @PostConstruct
    public void addDummyData() {
        // Adds dummy data to the database
        SellingCompany sellingCompany = new SellingCompany("Selling Company 1");
        entityManager.persist(sellingCompany);

        SellingCompany sellingCompany2 = new SellingCompany("Selling Company 2");
        Product product1 = new Product("Product 1", "Product 1 description", 4000,10, sellingCompany2);
        Product product2 = new Product("Product 2", "Product 2 description", 5000,10, sellingCompany2);
        sellingCompany2.addProduct(product1);
        sellingCompany2.addProduct(product2);
        entityManager.persist(sellingCompany2);

        entityManager.persist(product1);
        entityManager.persist(product2);
    }

    public void addSellingCompany(SellingCompany sellingCompany) {
        entityManager.persist(sellingCompany);
    }

    public SellingCompany getSellingCompanyById(int id) {
        return entityManager.find(SellingCompany.class, id);
    }

    public List<SellingCompany> getAllSellingCompanies() {
        return entityManager.createQuery("SELECT s FROM SellingCompany s", SellingCompany.class).getResultList();
    }

    public void updateSellingCompany(SellingCompany sellingCompany) {
        entityManager.merge(sellingCompany);
    }

    public void deleteSellingCompany(SellingCompany sellingCompany) {
        entityManager.remove(sellingCompany);
    }

    public void addProductToSellingCompany(SellingCompany sellingCompany, Product product) {
        sellingCompany.addProduct(product);
        entityManager.merge(sellingCompany);
    }

    public List<Product> getProductsBySellingCompany(int sellingCompanyId) {
        return entityManager.createQuery("SELECT p FROM Product p WHERE p.sellingCompany.id = :sellingCompanyId", Product.class)
                .setParameter("sellingCompanyId", sellingCompanyId)
                .getResultList();
    }

}
