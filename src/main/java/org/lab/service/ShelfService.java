package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.Shelf;
import org.lab.repository.ShelfRepository;

import java.util.List;

@Stateless
public class ShelfService {
    @Inject
    private ShelfRepository shelfRepository;

    public List<Shelf> findAll() {
        return shelfRepository.findAll();
    }

    public Shelf findById(int id) {
        return shelfRepository.findById(id);
    }

    public void save(Shelf shelf) {
        shelfRepository.save(shelf);
    }

    public void delete(Shelf shelf) {
        shelfRepository.delete(shelf.getId());
    }
}
