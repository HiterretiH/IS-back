package org.lab.repository;

import jakarta.ejb.Stateless;
import org.lab.model.Product;

@Stateless
public class ProductRepository extends GenericRepository<Product, Integer> {
    public ProductRepository() {
        super(Product.class);
    }
}
