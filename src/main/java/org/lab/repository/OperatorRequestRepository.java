package org.lab.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.lab.model.OperatorRequest;

import java.util.List;

@Stateless
public class OperatorRequestRepository extends GenericRepository<OperatorRequest, Integer> {

    public OperatorRequestRepository() {
        super(OperatorRequest.class);
    }

    public List<OperatorRequest> findAllPending() {
        Query query = entityManager.createNativeQuery(
                "SELECT * FROM find_all_pending_operator_requests()", OperatorRequest.class);
        return query.getResultList();
    }
}
