package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.LoadersAndShelves;
import org.lab.repository.LoadersAndShelvesRepository;

import java.util.List;

@Stateless
public class LoadersAndShelvesService {
    @Inject
    private LoadersAndShelvesRepository loadersAndShelvesRepository;

    public List<LoadersAndShelves> findAll() {
        return loadersAndShelvesRepository.findAll();
    }

    public LoadersAndShelves findById(int id) {
        return loadersAndShelvesRepository.findById(id);
    }

    public void save(LoadersAndShelves loadersAndShelves) {
        loadersAndShelvesRepository.save(loadersAndShelves);
    }

    public void delete(LoadersAndShelves loadersAndShelves) {
        loadersAndShelvesRepository.delete(loadersAndShelves.getId());
    }
}
