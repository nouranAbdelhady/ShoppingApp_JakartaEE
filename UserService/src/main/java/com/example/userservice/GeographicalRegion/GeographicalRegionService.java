package com.example.userservice.GeographicalRegion;

import com.example.userservice.User.Userr;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import java.util.List;

@Stateless
public class GeographicalRegionService {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("userr");

    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    public List<GeographicalRegion> getAllRegions() {
        TypedQuery<GeographicalRegion> query = entityManager.createQuery("SELECT r FROM GeographicalRegion r", GeographicalRegion.class);
        return query.getResultList();
    }

    public GeographicalRegion getRegionById(int id) {
        return entityManager.find(GeographicalRegion.class, id);
    }

    public GeographicalRegion getRegionByName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        TypedQuery<GeographicalRegion> query = entityManager.createQuery("SELECT r FROM GeographicalRegion r WHERE r.name = :name", GeographicalRegion.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

    public void addRegion(GeographicalRegion region) {
        entityManager.getTransaction().begin();
        entityManager.persist(region);
        entityManager.getTransaction().commit();
    }

    public void updateRegion(GeographicalRegion region) {
        entityManager.getTransaction().begin();
        entityManager.merge(region);
        entityManager.getTransaction().commit();
    }

    public void deleteRegion(int id) {
        GeographicalRegion region = getRegionById(id);
        entityManager.getTransaction().begin();
        entityManager.remove(region);
        entityManager.getTransaction().commit();
    }


    public List<Userr> getAllCustomersForRegion(GeographicalRegion region) {
        TypedQuery<Userr> query = entityManager.createQuery("SELECT c FROM Userr c WHERE c.geographicalRegion = :region", Userr.class);
        query.setParameter("region", region);
        return query.getResultList();
    }
}
