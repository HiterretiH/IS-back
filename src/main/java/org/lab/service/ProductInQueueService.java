package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.ProductInQueue;
import org.lab.repository.ProductInQueueRepository;

import java.util.List;

@Stateless
public class ProductInQueueService {
    @Inject
    private ProductInQueueRepository productInQueueRepository;

    public List<ProductInQueue> findAll() {
        return productInQueueRepository.findAll();
    }

    public ProductInQueue findById(int id) {
        return productInQueueRepository.findById(id);
    }

    public void save(ProductInQueue productInQueue) {
        productInQueueRepository.save(productInQueue);
    }

    public void delete(ProductInQueue productInQueue) {
        productInQueueRepository.delete(productInQueue.getId());
    }
}
