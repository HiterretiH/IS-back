package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.OperatorRequest;
import org.lab.model.SortingRequest;
import org.lab.repository.SortingRequestRepository;

import java.util.List;

@Stateless
public class SortingRequestService {
    @Inject
    private SortingRequestRepository sortingRequestRepository;

    public List<SortingRequest> getAll() {
        return sortingRequestRepository.findAll();
    }

    public SortingRequest getById(int id) {
        return sortingRequestRepository.findById(id);
    }

    public void create(SortingRequest sortingRequest) {
        sortingRequestRepository.save(sortingRequest);
    }

    public void delete(SortingRequest sortingRequest) {
        sortingRequestRepository.delete(sortingRequest.getId());
    }

    // Отказаться от заявки, система автоматически перераспределяет заявку
    public void reject(SortingRequest sortingRequest) {}
}
