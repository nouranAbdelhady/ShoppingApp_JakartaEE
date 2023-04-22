package com.example.productservice.ShippingCompany;

import com.example.productservice.Product.Product;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class SellingCompanyService {

    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("product");

    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    public void addSellingCompany(SellingCompany sellingCompany) {
        entityManager.getTransaction().begin();
        entityManager.persist(sellingCompany);
        entityManager.getTransaction().commit();;
    }

    public SellingCompany getSellingCompanyById(int id) {
        return entityManager.find(SellingCompany.class, id);
    }

    public List<SellingCompany> getAllSellingCompanies() {
        return entityManager.createQuery("SELECT s FROM SellingCompany s", SellingCompany.class).getResultList();
    }

    public void updateSellingCompany(SellingCompany sellingCompany) {
        entityManager.getTransaction().begin();
        entityManager.merge(sellingCompany);
        entityManager.getTransaction().commit();
    }

    public void deleteSellingCompany(SellingCompany sellingCompany) {
        entityManager.getTransaction().begin();
        entityManager.remove(sellingCompany);
        entityManager.getTransaction().commit();
    }

    public void addProductToSellingCompany(SellingCompany sellingCompany, Product product) {
        sellingCompany.addProduct(product);
        entityManager.getTransaction().begin();
        entityManager.merge(sellingCompany);
        entityManager.getTransaction().commit();
    }

    public List<Product> getProductsBySellingCompany(int sellingCompanyId) {
        return entityManager.createQuery("SELECT p FROM Product p WHERE p.sellingCompany.id = :sellingCompanyId", Product.class)
                .setParameter("sellingCompanyId", sellingCompanyId)
                .getResultList();
    }

    public boolean login (String name, String password) {
        SellingCompany sellingCompany = entityManager.createQuery("SELECT s FROM SellingCompany s WHERE s.name = :name", SellingCompany.class)
                .setParameter("name", name)
                .getSingleResult();
        if (sellingCompany.getPassword().equals(password)) {
            sellingCompany.setIs_logged_in(true);
            entityManager.getTransaction().begin();
            entityManager.merge(sellingCompany);
            entityManager.getTransaction().commit();
            return true;
        }
        return false;
    }

    public boolean logout (String name) {
        SellingCompany sellingCompany = entityManager.createQuery("SELECT s FROM SellingCompany s WHERE s.name = :name", SellingCompany.class)
                .setParameter("name", name)
                .getSingleResult();
        if (sellingCompany.getIs_logged_in()) {
            sellingCompany.setIs_logged_in(false);
            entityManager.getTransaction().begin();
            entityManager.merge(sellingCompany);
            entityManager.getTransaction().commit();
            return true;
        }
        return false;
    }

    public boolean deleteProductFromSellingCompany(int sellingCompanyId, int productId) {
        SellingCompany sellingCompany = getSellingCompanyById(sellingCompanyId);
        Product product = entityManager.createQuery("SELECT p FROM Product p WHERE p.id = :productId", Product.class)
                .setParameter("productId", productId)
                .getSingleResult();
        if (sellingCompany.getProducts().contains(product)) {
            sellingCompany.removeProduct(product);
            entityManager.getTransaction().begin();
            entityManager.merge(sellingCompany);

            //remove from products table
            entityManager.remove(product);
            entityManager.getTransaction().commit();
            return true;
        }
        return false;
    }

}
