package org.lab.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import org.lab.model.Manager;
import org.lab.model.WarehouseOperator;

@Stateless
public class ManagerRepository extends GenericRepository<Manager, Integer> {
    public ManagerRepository() {
        super(Manager.class);
    }

    public Manager findByUserId(Integer userId) {
        TypedQuery<Manager> query = entityManager.createQuery(
                "SELECT m FROM Manager m WHERE m.appUser.id = :userId", Manager.class);
        query.setParameter("userId", userId);
        return query.getResultStream().findFirst().orElse(null);
    }
}
