package org.lab.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import org.lab.model.Queue;
import org.lab.model.SortingStation;

import java.util.List;

@Stateless
public class QueueRepository extends GenericRepository<Queue, Integer> {
    public QueueRepository() {
        super(Queue.class);
    }

    public List<Queue> findBySortingStation(SortingStation sortingStation) {
        String jpql = "SELECT q FROM Queue q WHERE q.sortingStation = :sortingStation";
        TypedQuery<Queue> query = entityManager.createQuery(jpql, Queue.class);
        query.setParameter("sortingStation", sortingStation);

        return query.getResultList();
    }
}
