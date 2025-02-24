package org.lab.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.Query;
import org.lab.model.Location;

import java.util.List;

@Stateless
public class LocationRepository extends GenericRepository<Location, Integer> {
    public LocationRepository() {
        super(Location.class);
    }

    public List<Location> findWithPagination(int page, int size) {
        Query query = entityManager.createNativeQuery("SELECT * FROM get_locations_paginated(:page, :size)", Location.class);
        query.setParameter("page", page);
        query.setParameter("size", size);
        return query.getResultList();
    }

    public Integer count() {
        Query query = entityManager.createNativeQuery("SELECT * FROM get_locations_count()");
        return (Integer) query.getSingleResult();
    }
}
