package org.lab.repository;

import jakarta.ejb.Stateless;
import org.lab.model.ProductInQueue;

@Stateless
public class ProductInQueueRepository extends GenericRepository<ProductInQueue, Integer> {
    public ProductInQueueRepository() {
        super(ProductInQueue.class);
    }
}
