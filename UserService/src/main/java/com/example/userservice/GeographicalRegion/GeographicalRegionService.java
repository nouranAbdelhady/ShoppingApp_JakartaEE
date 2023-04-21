package com.example.userservice.GeographicalRegion;

import com.example.userservice.User.Userr;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class GeographicalRegionService {
    @PersistenceContext(unitName = "userr")
    private EntityManager entityManager;

    @PostConstruct
    public void addDummyData() {
        // Add dummy data for testing
        GeographicalRegion region1 = new GeographicalRegion("Zamalek");
        GeographicalRegion region2 = new GeographicalRegion("Agouza");
        GeographicalRegion region3 = new GeographicalRegion("Heliopolis");
        GeographicalRegion region4 = new GeographicalRegion("Nasr City");
        GeographicalRegion region5 = new GeographicalRegion("New Cairo");
        entityManager.persist(region1);
        entityManager.persist(region2);
        entityManager.persist(region3);
        entityManager.persist(region4);
        entityManager.persist(region5);
    }

    public List<GeographicalRegion> getAllRegions() {
        TypedQuery<GeographicalRegion> query = entityManager.createQuery("SELECT r FROM GeographicalRegion r", GeographicalRegion.class);
        return query.getResultList();
    }

    public GeographicalRegion getRegionById(int id) {
        return entityManager.find(GeographicalRegion.class, (long) id);
    }

    public GeographicalRegion getRegionByName(String name) {
        TypedQuery<GeographicalRegion> query = entityManager.createQuery("SELECT r FROM GeographicalRegion r WHERE r.name = :name", GeographicalRegion.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

    public void addRegion(GeographicalRegion region) {
        entityManager.persist(region);
    }

    public void updateRegion(GeographicalRegion region) {
        entityManager.merge(region);
    }

    public void deleteRegion(int id) {
        GeographicalRegion region = getRegionById(id);
        entityManager.remove(region);
    }


    public List<Userr> getAllCustomersForRegion(GeographicalRegion region) {
        TypedQuery<Userr> query = entityManager.createQuery("SELECT c FROM Userr c WHERE c.geographicalRegion = :region", Userr.class);
        query.setParameter("region", region);
        return query.getResultList();
    }
}
