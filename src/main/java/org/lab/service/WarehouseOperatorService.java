package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.WarehouseOperator;
import org.lab.repository.WarehouseOperatorRepository;

import java.util.List;

@Stateless
public class WarehouseOperatorService {
    @Inject
    private WarehouseOperatorRepository warehouseOperatorRepository;

    public List<WarehouseOperator> findAll() {
        return warehouseOperatorRepository.findAll();
    }

    public WarehouseOperator findById(int id) {
        return warehouseOperatorRepository.findById(id);
    }

    public void save(WarehouseOperator warehouseOperator) {
        warehouseOperatorRepository.save(warehouseOperator);
    }

    public void delete(WarehouseOperator warehouseOperator) {
        warehouseOperatorRepository.delete(warehouseOperator.getId());
    }
}
