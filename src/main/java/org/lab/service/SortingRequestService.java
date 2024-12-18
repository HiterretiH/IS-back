package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.SortingRequest;
import org.lab.repository.SortingRequestRepository;

import java.util.List;

@Stateless
public class SortingRequestService {
    @Inject
    private SortingRequestRepository sortingRequestRepository;

    public List<SortingRequest> findAll() {
        return sortingRequestRepository.findAll();
    }

    public SortingRequest findById(int id) {
        return sortingRequestRepository.findById(id);
    }

    public void save(SortingRequest sortingRequest) {
        sortingRequestRepository.save(sortingRequest);
    }

    public void delete(SortingRequest sortingRequest) {
        sortingRequestRepository.delete(sortingRequest.getId());
    }
}
