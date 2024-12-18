package org.lab.repository;

import jakarta.ejb.Stateless;
import org.lab.model.Warehouse;

@Stateless
public class WarehouseRepository extends GenericRepository<Warehouse, Integer> {
    public WarehouseRepository() {
        super(Warehouse.class);
    }
}
