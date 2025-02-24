package org.lab.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.Query;
import org.lab.model.Warehouse;
import org.lab.model.Worker;

import java.util.List;

@Stateless
public class WarehouseRepository extends GenericRepository<Warehouse, Integer> {
    public WarehouseRepository() {
        super(Warehouse.class);
    }

    public List<String> findWithPagination(int page, int size) {
        Query query = entityManager.createNativeQuery("SELECT * FROM get_warehouses_paginated(:page, :size)");
        query.setParameter("page", page);
        query.setParameter("size", size);

        return query.getResultList();
    }


    public Integer count() {
        Query query = entityManager.createNativeQuery("SELECT * FROM get_warehouses_count()");
        return (Integer) query.getSingleResult();
    }
}
