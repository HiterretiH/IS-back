package org.lab.repository;

import jakarta.ejb.Stateless;
import org.lab.model.Shelf;

@Stateless
public class ShelfRepository extends GenericRepository<Shelf, Integer> {
    public ShelfRepository() {
        super(Shelf.class);
    }
}
