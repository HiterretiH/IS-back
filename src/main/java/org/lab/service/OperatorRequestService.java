package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.OperatorRequest;
import org.lab.repository.OperatorRequestRepository;

import java.util.List;

@Stateless
public class OperatorRequestService {
    @Inject
    private OperatorRequestRepository operatorRequestRepository;

    public List<OperatorRequest> findAll() {
        return operatorRequestRepository.findAll();
    }

    public OperatorRequest findById(int id) {
        return operatorRequestRepository.findById(id);
    }

    public void save(OperatorRequest operatorRequest) {
        operatorRequestRepository.save(operatorRequest);
    }

    public void delete(OperatorRequest operatorRequest) {
        operatorRequestRepository.delete(operatorRequest.getId());
    }
}
