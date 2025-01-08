package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.Queue;
import org.lab.model.SortingStation;
import org.lab.repository.QueueRepository;

import java.util.List;

@Stateless
public class QueueService {
    @Inject
    private QueueRepository queueRepository;

    public List<Queue> getAll() {
        return queueRepository.findAll();
    }

    public Queue getById(int id) {
        return queueRepository.findById(id);
    }

    public void create(Queue queue) {
        queueRepository.save(queue);
    }

    public void delete(Queue queue) {
        queueRepository.delete(queue.getId());
    }

    public void setSortingStation(SortingStation sortingStation) {}
    public void getAllProducts(Queue queue) {}

    // Остановить обработку продуктов, вернуть на склад
    public void interrupt(Queue queue) {}
}
