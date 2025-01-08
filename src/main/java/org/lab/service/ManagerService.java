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

    public List<Manager> getAll() {
        return managerRepository.findAll();
    }

    public Manager getById(int id) {
        return managerRepository.findById(id);
    }

    public void create(Manager manager) {
        managerRepository.save(manager);
    }

    public void delete(Manager manager) {
        managerRepository.delete(manager.getId());
    }

    // Передать позицию менеджера другому пользователю, снимая себя с должности
    public void reassign(Manager manager) {
        managerRepository.update(manager);
    }
}
