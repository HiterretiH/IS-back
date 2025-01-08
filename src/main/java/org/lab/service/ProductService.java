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

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getById(int id) {
        return productRepository.findById(id);
    }

    public void create(Product product) {
        productRepository.save(product);
    }

    public void delete(Product product) {
        productRepository.delete(product.getId());
    }

    // Отправить в сортировку, при успехе изменить состояние
    public void ship(Product product) {}

    // Изменить состояние
    public void dispose(Product product) {}

    // Определить станцию для сортировки, дождаться очереди или создать новую, обработать, учитывая производительность станции
    public void sortToStore(Product product) {}
    public void sortToShip(Product product) {}

    // Изменить приоритет, используемый при сортировке
    public void setPriority(Product product) {}
}
