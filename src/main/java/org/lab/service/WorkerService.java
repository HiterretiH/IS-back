package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.WarehouseOperator;
import org.lab.model.Worker;
import org.lab.repository.WorkerRepository;

import java.util.List;

@Stateless
public class WorkerService {
    @Inject
    private WorkerRepository workerRepository;

    public List<Worker> getAll() {return workerRepository.findAll();}

    public Worker getById(int id) {return workerRepository.findById(id);}

    public void create(Worker worker) {workerRepository.save(worker);}

    public void delete(Worker worker) {workerRepository.delete(worker.getId());}

    public void fire(WarehouseOperator warehouseOperator) {}
}
