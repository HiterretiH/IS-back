package org.lab.repository;

import jakarta.ejb.Stateless;
import org.lab.model.ProductType;

@Stateless
public class ProductTypeRepository extends GenericRepository<ProductType, Integer> {
    public ProductTypeRepository() {
        super(ProductType.class);
    }
}
