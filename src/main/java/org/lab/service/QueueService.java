package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.Product;
import org.lab.model.ProductState;
import org.lab.model.Queue;
import org.lab.model.SortingStation;
import org.lab.repository.ProductRepository;
import org.lab.repository.QueueRepository;
import org.lab.utils.PaginatedResponse;

import java.util.List;

@Stateless
public class QueueService {
    @Inject
    private QueueRepository queueRepository;

    @Inject
    private ProductRepository productRepository;

    public PaginatedResponse<String> getAll(int page, int size) {
        List<String> data = queueRepository.findWithPagination(page, size);
        int count = queueRepository.count();
        return new PaginatedResponse<>(data, count);
    }

    public Queue getById(int id) {
        return queueRepository.findById(id);
    }

    public void create(Queue queue) {
        Queue newQueue = new Queue();
        newQueue.setCapacity(queue.getCapacity());
        newQueue.setSortingStation(queue.getSortingStation());

        queueRepository.save(newQueue);
    }

    public void update(Queue queue) {queueRepository.update(queue);}

    public void delete(Queue queue) {
        queueRepository.delete(queue.getId());
    }

    public List<Product> getAllProducts(Queue queue) {
        return productRepository.findAllInQueue(queue.getId());
    }

    public void interrupt(Queue queue) {
        for (Product product : getAllProducts(queue)) {
            product.setQueue(null);
            productRepository.update(product);
        }
    }
}
