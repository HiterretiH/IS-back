package org.lab.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.Query;
import org.lab.model.Worker;

import java.util.List;

@Stateless
public class WorkerRepository extends GenericRepository<Worker, Integer> {
    public WorkerRepository() {
        super(Worker.class);
    }

    public List<String> findWithPagination(int page, int size) {
        Query query = entityManager.createNativeQuery("SELECT * FROM get_workers_paginated(:page, :size)");
        query.setParameter("page", page);
        query.setParameter("size", size);

        return query.getResultList();
    }


    public Integer count() {
        Query query = entityManager.createNativeQuery("SELECT * FROM get_workers_count()");
        return (Integer) query.getSingleResult();
    }
}
