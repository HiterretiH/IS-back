package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.Status;
import org.lab.model.Warehouse;
import org.lab.model.Worker;
import org.lab.repository.WorkerRepository;
import org.lab.utils.PaginatedResponse;

import java.util.List;

@Stateless
public class WorkerService {
    @Inject
    private WorkerRepository workerRepository;

    public PaginatedResponse<String> getAll(int page, int size) {
        List<String> data = workerRepository.findWithPagination(page, size);
        int count = workerRepository.count();
        return new PaginatedResponse<>(data, count);
    }

    public Worker getById(int id) {return workerRepository.findById(id);}

    public void create(Worker worker) {
        Worker newWorker = new Worker();
        newWorker.setFirstName(worker.getFirstName());
        newWorker.setLastName(worker.getLastName());
        newWorker.setMiddleName(worker.getMiddleName());
        newWorker.setBirthDate(worker.getBirthDate());
        newWorker.setHireDate(worker.getHireDate());
        newWorker.setStatus(worker.getStatus());
        newWorker.setWarehouse(worker.getWarehouse());

        workerRepository.save(newWorker);
    }

    public void update(Worker worker) {
        workerRepository.update(worker);
    }

    public void delete(Worker worker) {workerRepository.delete(worker.getId());}

    public void hire(Worker worker) {
        worker.setStatus(Status.HIRED);
        workerRepository.update(worker);
    }

    public void reject(Worker worker) {
        worker.setStatus(Status.REJECTED);
        workerRepository.update(worker);
    }

    public void fire(Worker worker) {
        worker.setStatus(Status.FIRED);
        workerRepository.update(worker);
    }
}
