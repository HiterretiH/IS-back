package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.lab.model.OperatorRequest;
import org.lab.model.RequestState;
import org.lab.repository.OperatorRequestRepository;

import java.util.List;

@Stateless
public class OperatorRequestService {
    @Inject
    private OperatorRequestRepository operatorRequestRepository;

    public List<OperatorRequest> getAll() {
        return operatorRequestRepository.findAll();
    }

    public OperatorRequest getById(int id) {
        return operatorRequestRepository.findById(id);
    }

    public void create(OperatorRequest operatorRequest) {
        operatorRequestRepository.save(operatorRequest);
    }

    public void update(OperatorRequest operatorRequest) {
        operatorRequestRepository.update(operatorRequest);
    }

    public void delete(OperatorRequest operatorRequest) {
        operatorRequestRepository.delete(operatorRequest.getId());
    }

    public List<OperatorRequest> getAllPending() {
        return operatorRequestRepository.findAllPending();
    }
    @Transactional
    public void approve(OperatorRequest operatorRequest) {
        operatorRequest.setStatus(RequestState.ACCEPTED);
        this.update(operatorRequest);
    }
    @Transactional
    public void reject(OperatorRequest operatorRequest) {
        operatorRequest.setStatus(RequestState.REJECTED);
        this.update(operatorRequest);
    }
}
