package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.Location;
import org.lab.repository.LocationRepository;

import java.util.List;

@Stateless
public class LocationService {
    @Inject
    private LocationRepository locationRepository;

    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    public Location findById(int id) {
        return locationRepository.findById(id);
    }

    public void save(Location location) {
        locationRepository.save(location);
    }

    public void delete(Location location) {
        locationRepository.delete(location.getId());
    }
}
