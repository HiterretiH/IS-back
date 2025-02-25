package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.ProductType;
import org.lab.repository.ProductTypeRepository;
import org.lab.utils.PaginatedResponse;

import java.util.List;

@Stateless
public class ProductTypeService {
    @Inject
    private ProductTypeRepository productTypeRepository;

    public PaginatedResponse<ProductType> getAll(int page, int size) {
        List<ProductType> data = productTypeRepository.findWithPagination(page, size);
        int count = productTypeRepository.count();
        return new PaginatedResponse<>(data, count);
    }

    public ProductType getById(int id) {
        return productTypeRepository.findById(id);
    }

    public void create(ProductType productType) {
        ProductType newProductType = new ProductType();
        newProductType.setName(productType.getName());

        productTypeRepository.save(newProductType);
    }

    public void delete(ProductType productType) {
        productTypeRepository.delete(productType.getId());
    }
}
