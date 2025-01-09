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
}
