package org.lab.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.Query;
import org.lab.model.SortingStation;

import java.util.List;

@Stateless
public class SortingStationRepository extends GenericRepository<SortingStation, Integer> {
    public SortingStationRepository() {
        super(SortingStation.class);
    }

    public List<String> findWithPagination(int page, int size) {
        Query query = entityManager.createNativeQuery("SELECT * FROM get_sorting_stations_paginated(:page, :size)");
        query.setParameter("page", page);
        query.setParameter("size", size);

        return query.getResultList();
    }

    public Integer count() {
        Query query = entityManager.createNativeQuery("SELECT * FROM get_sorting_stations_count()");
        return (Integer) query.getSingleResult();
    }

    public Integer countQueuesBySortingStation(Integer sortingStationId) {
        Query query = entityManager.createNativeQuery("SELECT * FROM get_queues_count_by_sorting_station(:sortingStationId)");
        query.setParameter("sortingStationId", sortingStationId);
        return (Integer) query.getSingleResult();
    }
}
