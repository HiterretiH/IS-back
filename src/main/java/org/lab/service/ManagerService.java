package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.Manager;
import org.lab.repository.ManagerRepository;

import java.util.List;

@Stateless
public class ManagerService {
    @Inject
    private ManagerRepository managerRepository;

    public List<Manager> findAll() {
        return managerRepository.findAll();
    }

    public Manager findById(int id) {
        return managerRepository.findById(id);
    }

    public void save(Manager manager) {
        managerRepository.save(manager);
    }

    public void delete(Manager manager) {
        managerRepository.delete(manager.getId());
    }
}
