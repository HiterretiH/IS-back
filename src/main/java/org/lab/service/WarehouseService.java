package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.Warehouse;
import org.lab.repository.WarehouseRepository;

import java.util.List;

@Stateless
public class WarehouseService {
    @Inject
    private WarehouseRepository warehouseRepository;

    public List<Warehouse> findAll() {
        return warehouseRepository.findAll();
    }

    public Warehouse findById(int id) {
        return warehouseRepository.findById(id);
    }

    public void save(Warehouse warehouse) {
        warehouseRepository.save(warehouse);
    }

    public void delete(Warehouse warehouse) {
        warehouseRepository.delete(warehouse.getId());
    }
}
