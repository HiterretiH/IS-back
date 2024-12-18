package org.lab.repository;

import jakarta.ejb.Stateless;
import org.lab.model.WarehouseOperator;

@Stateless
public class WarehouseOperatorRepository extends GenericRepository<WarehouseOperator, Integer> {
    public WarehouseOperatorRepository() {
        super(WarehouseOperator.class);
    }
}
