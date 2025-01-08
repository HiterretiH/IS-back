package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.SortingStation;
import org.lab.repository.SortingStationRepository;

import java.util.List;

@Stateless
public class SortingStationService {
    @Inject
    private SortingStationRepository sortingStationRepository;

    public List<SortingStation> getAll() {
        return sortingStationRepository.findAll();
    }

    public SortingStation getById(int id) {
        return sortingStationRepository.findById(id);
    }

    public void create(SortingStation sortingStation) {
        sortingStationRepository.save(sortingStation);
    }

    public void delete(SortingStation sortingStation) {
        sortingStationRepository.delete(sortingStation.getId());
    }
}
