package org.lab.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.lab.model.OperatorRequest;
import org.lab.model.User;

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

    public List<OperatorRequest> findAllByUserId(int id) {
        try {
            Query query = entityManager.createNativeQuery(
                    "SELECT * FROM find_all_operator_requests_by_user(:user_id)", OperatorRequest.class);
            query.setParameter("user_id", id);
            System.out.println(query.getResultList());
            return (List<OperatorRequest>) query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
