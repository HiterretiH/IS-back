package org.lab.repository;

import jakarta.ejb.Stateless;
import org.lab.model.SortingStation;

@Stateless
public class SortingStationRepository extends GenericRepository<SortingStation, Integer> {
    public SortingStationRepository() {
        super(SortingStation.class);
    }
}
