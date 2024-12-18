package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.Worker;
import org.lab.repository.WorkerRepository;

import java.util.List;

@Stateless
public class WorkerService {
    @Inject
    private WorkerRepository workerRepository;

    public List<Worker> findAll() {return workerRepository.findAll();}

    public Worker findById(int id) {return workerRepository.findById(id);}

    public void save(Worker worker) {workerRepository.save(worker);}

    public void delete(Worker worker) {workerRepository.delete(worker.getId());}
}
