package org.lab.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import org.lab.model.WarehouseOperator;

@Stateless
public class WarehouseOperatorRepository extends GenericRepository<WarehouseOperator, Integer> {
    public WarehouseOperatorRepository() {
        super(WarehouseOperator.class);
    }

    public WarehouseOperator findByUserId(Integer userId) {
        TypedQuery<WarehouseOperator> query = entityManager.createQuery(
                "SELECT wo FROM WarehouseOperator wo WHERE wo.appUser.id = :userId", WarehouseOperator.class);
        query.setParameter("userId", userId);
        return query.getResultStream().findFirst().orElse(null);
    }
}
