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

    public List<Shelf> getAll() {
        return shelfRepository.findAll();
    }

    public Shelf getById(int id) {
        return shelfRepository.findById(id);
    }

    public void create(Shelf shelf) {
        shelfRepository.save(shelf);
    }

    public void delete(Shelf shelf) {
        shelfRepository.delete(shelf.getId());
    }
}
