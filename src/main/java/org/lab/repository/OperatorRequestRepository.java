package org.lab.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import org.lab.model.OperatorRequest;
import org.lab.model.RequestState;

import java.util.List;

@Stateless
public class OperatorRequestRepository extends GenericRepository<OperatorRequest, Integer> {
    public OperatorRequestRepository() {
        super(OperatorRequest.class);
    }

    public List<OperatorRequest> findAllPending() {
        String jpql = "SELECT o FROM OperatorRequest o WHERE o.status = :status";
        TypedQuery<OperatorRequest> query = entityManager.createQuery(jpql, OperatorRequest.class);
        query.setParameter("status", RequestState.PENDING);
        return query.getResultList();
    }
}
