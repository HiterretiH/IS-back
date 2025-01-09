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
}
