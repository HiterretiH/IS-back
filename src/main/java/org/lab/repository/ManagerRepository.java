package org.lab.repository;

import jakarta.ejb.Stateless;
import org.lab.model.Manager;

@Stateless
public class ManagerRepository extends GenericRepository<Manager, Integer> {
    public ManagerRepository() {
        super(Manager.class);
    }
}
