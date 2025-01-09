package org.lab.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.lab.model.WarehouseOperator;

@Stateless
public class WarehouseOperatorRepository extends GenericRepository<WarehouseOperator, Integer> {

    public WarehouseOperatorRepository() {
        super(WarehouseOperator.class);
    }

    public WarehouseOperator findByUserId(Integer userId) {
        try {
            Query query = entityManager.createNativeQuery(
                    "SELECT * FROM find_warehouse_operator_by_user_id(:userId)", WarehouseOperator.class);
            query.setParameter("userId", userId);

            return (WarehouseOperator) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
