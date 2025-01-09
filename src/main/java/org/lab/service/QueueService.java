package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.Product;
import org.lab.model.ProductState;
import org.lab.model.Queue;
import org.lab.model.SortingStation;
import org.lab.repository.ProductRepository;
import org.lab.repository.QueueRepository;

import java.util.List;

@Stateless
public class QueueService {
    @Inject
    private QueueRepository queueRepository;

    @Inject
    private ProductRepository productRepository;

    public List<Queue> getAll() {
        return queueRepository.findAll();
    }

    public Queue getById(int id) {
        return queueRepository.findById(id);
    }

    public void create(Queue queue) {
        queueRepository.save(queue);
    }

    public void update(Queue queue) {queueRepository.update(queue);}

    public void delete(Queue queue) {
        queueRepository.delete(queue.getId());
    }
}
