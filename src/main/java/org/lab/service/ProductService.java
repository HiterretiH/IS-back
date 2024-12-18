package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.Product;
import org.lab.repository.ProductRepository;

import java.util.List;

@Stateless
public class ProductService {
    @Inject
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(int id) {
        return productRepository.findById(id);
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public void delete(Product product) {
        productRepository.delete(product.getId());
    }
}
