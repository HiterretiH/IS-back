package org.lab.repository;

import jakarta.ejb.Stateless;
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

    public OperatorRequest getByOperatorId(int operatorId) {
        Query query = entityManager.createNativeQuery(
                        "SELECT * FROM get_operator_request_by_operator_id(:operatorId)", OperatorRequest.class)
                .setParameter("operatorId", operatorId);
        return (OperatorRequest) query.getSingleResult();
    }
}
