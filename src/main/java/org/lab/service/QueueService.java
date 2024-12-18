package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.Queue;
import org.lab.repository.QueueRepository;

import java.util.List;

@Stateless
public class QueueService {
    @Inject
    private QueueRepository queueRepository;

    public List<Queue> findAll() {
        return queueRepository.findAll();
    }

    public Queue findById(int id) {
        return queueRepository.findById(id);
    }

    public void save(Queue queue) {
        queueRepository.save(queue);
    }

    public void delete(Queue queue) {
        queueRepository.delete(queue.getId());
    }
}
