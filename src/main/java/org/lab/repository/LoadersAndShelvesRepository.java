package org.lab.repository;

import jakarta.ejb.Stateless;
import org.lab.model.LoadersAndShelves;

@Stateless
public class LoadersAndShelvesRepository extends GenericRepository<LoadersAndShelves, Integer> {
    public LoadersAndShelvesRepository() {
        super(LoadersAndShelves.class);
    }
}
