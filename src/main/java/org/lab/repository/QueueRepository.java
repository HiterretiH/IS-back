package org.lab.repository;

import jakarta.ejb.Stateless;
import org.lab.model.Queue;

@Stateless
public class QueueRepository extends GenericRepository<Queue, Integer> {
    public QueueRepository() {
        super(Queue.class);
    }
}
