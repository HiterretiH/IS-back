package org.lab.repository;

import jakarta.ejb.Stateless;
import org.lab.model.Worker;

@Stateless
public class WorkerRepository extends GenericRepository<Worker, Integer> {
    public WorkerRepository() {
        super(Worker.class);
    }
}
