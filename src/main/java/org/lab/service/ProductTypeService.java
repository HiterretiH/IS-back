package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.ProductType;
import org.lab.repository.ProductTypeRepository;

import java.util.List;

@Stateless
public class ProductTypeService {
    @Inject
    private ProductTypeRepository productTypeRepository;

    public List<ProductType> findAll() {
        return productTypeRepository.findAll();
    }

    public ProductType findById(int id) {
        return productTypeRepository.findById(id);
    }

    public void save(ProductType productType) {
        productTypeRepository.save(productType);
    }

    public void delete(ProductType productType) {
        productTypeRepository.delete(productType.getId());
    }
}
