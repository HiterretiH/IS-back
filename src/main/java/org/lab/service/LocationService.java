package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.Location;
import org.lab.repository.LocationRepository;
import org.lab.utils.PaginatedResponse;

import java.util.List;

@Stateless
public class LocationService {
    @Inject
    private LocationRepository locationRepository;

    public PaginatedResponse<Location> getAll(int page, int size) {
        List<Location> data = locationRepository.findWithPagination(page, size);
        int count = locationRepository.count();
        return new PaginatedResponse<>(data, count);
    }

    public Location getById(int id) {
        return locationRepository.findById(id);
    }

    public void create(Location location) {
        locationRepository.save(location);
    }

    public void update(Location location) {
        locationRepository.update(location);
    }

    public void delete(Location location) {
        locationRepository.delete(location.getId());
    }
}
