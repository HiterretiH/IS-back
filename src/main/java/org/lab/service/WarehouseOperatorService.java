package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.ProductType;
import org.lab.model.Status;
import org.lab.model.WarehouseOperator;
import org.lab.repository.WarehouseOperatorRepository;

import java.util.List;

@Stateless
public class WarehouseOperatorService {
    @Inject
    private WarehouseOperatorRepository warehouseOperatorRepository;

    public List<WarehouseOperator> getAll() {
        return warehouseOperatorRepository.findAll();
    }

    public WarehouseOperator getById(int id) {
        return warehouseOperatorRepository.findById(id);
    }

    public void create(WarehouseOperator warehouseOperator) {
        warehouseOperatorRepository.save(warehouseOperator);
    }

    public void delete(WarehouseOperator warehouseOperator) {
        warehouseOperatorRepository.delete(warehouseOperator.getId());
    }

    public void fire(WarehouseOperator warehouseOperator) {
        warehouseOperator.setStatus(Status.FIRED);
        warehouseOperatorRepository.update(warehouseOperator);
    }

    public void assignProductType(WarehouseOperator warehouseOperator, ProductType productType) {
        warehouseOperator.setProductType(productType);
        warehouseOperatorRepository.update(warehouseOperator);
    }
}
