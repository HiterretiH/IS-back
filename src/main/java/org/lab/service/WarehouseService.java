package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.Partners;
import org.lab.model.SortingStation;
import org.lab.model.Warehouse;
import org.lab.repository.PartnersRepository;
import org.lab.repository.WarehouseRepository;
import org.lab.utils.PaginatedResponse;

import java.util.List;

@Stateless
public class WarehouseService {
    @Inject
    private WarehouseRepository warehouseRepository;

    public PaginatedResponse<String> getAll(int page, int size) {
        List<String> data = warehouseRepository.findWithPagination(page, size);
        int count = warehouseRepository.count();
        return new PaginatedResponse<>(data, count);
    }

    public Warehouse getById(int id) {
        return warehouseRepository.findById(id);
    }

    public void create(Warehouse warehouse) {
        Warehouse newWarehouse = new Warehouse();
        newWarehouse.setName(warehouse.getName());
        newWarehouse.setAddress(warehouse.getAddress());

        warehouseRepository.save(newWarehouse);
    }

    public void update(Warehouse warehouse) {
        warehouseRepository.update(warehouse);
    }

    public void delete(Warehouse warehouse) {
        warehouseRepository.delete(warehouse.getId());
    }
}
