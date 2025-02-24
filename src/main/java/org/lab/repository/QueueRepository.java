package org.lab.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.lab.model.Queue;
import org.lab.model.SortingStation;

import java.util.List;

@Stateless
public class QueueRepository extends GenericRepository<Queue, Integer> {

    public QueueRepository() {
        super(Queue.class);
    }

    public List<Queue> findBySortingStation(SortingStation sortingStation) {
        Query query = entityManager.createNativeQuery(
                "SELECT * FROM find_queues_by_sorting_station(:sortingStationId)", Queue.class);
        query.setParameter("sortingStationId", sortingStation.getId());
        return query.getResultList();
    }

    public List<String> findWithPagination(int page, int size) {
        Query query = entityManager.createNativeQuery("SELECT * FROM get_queues_paginated(:page, :size)");
        query.setParameter("page", page);
        query.setParameter("size", size);

        return query.getResultList();
    }


    public Integer count() {
        Query query = entityManager.createNativeQuery("SELECT * FROM get_queues_count()");
        return (Integer) query.getSingleResult();
    }
}
