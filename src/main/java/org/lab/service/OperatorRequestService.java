package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.lab.model.*;
import org.lab.repository.OperatorRequestRepository;
import org.lab.repository.ProductTypeRepository;
import org.lab.repository.UserRepository;
import org.lab.repository.WarehouseOperatorRepository;

import java.util.List;

@Stateless
public class OperatorRequestService {
    @Inject
    private OperatorRequestRepository operatorRequestRepository;
    @Inject
    private UserRepository userRepository;
    @Inject
    private ProductTypeRepository productTypeRepository;
    @Inject
    private WarehouseOperatorRepository warehouseOperatorRepository;

    public List<OperatorRequest> getAll() {
        return operatorRequestRepository.findAll();
    }
    public List<OperatorRequest> getAllByUserId(int id) {
        return operatorRequestRepository.findAllByUserId(id);
    }

    public OperatorRequest getById(int id) {
        return operatorRequestRepository.findById(id);
    }

    public OperatorRequest create(int id) {
        OperatorRequest operatorRequest = new OperatorRequest();
        User user = userRepository.findById(id);
        operatorRequest.setOperator(user);
        operatorRequest.setStatus(RequestState.PENDING);
        return operatorRequestRepository.save(operatorRequest);
    }

    public void update(OperatorRequest operatorRequest) {
        User user = operatorRequest.getOperator();
        if (user.getRole() == Role.USER && operatorRequest.getStatus() == RequestState.ACCEPTED) {
            user.setRole(Role.OPERATOR);
            userRepository.update(user);
        }

        operatorRequestRepository.update(operatorRequest);
    }

    public void delete(OperatorRequest operatorRequest) {
        operatorRequestRepository.delete(operatorRequest.getId());
    }

    public List<OperatorRequest> getAllPending() {
        return operatorRequestRepository.findAllPending();
    }

    @Transactional
    public void approve(OperatorRequest operatorRequest, ProductType productType) {
        operatorRequest.setStatus(RequestState.ACCEPTED);
        User user = operatorRequest.getOperator();
        user.setRole(Role.OPERATOR);
        userRepository.update(user);
        this.update(operatorRequest);

        WarehouseOperator operator = new WarehouseOperator();
        operator.setAppUser(user);
        operator.setProductType(productType);
        warehouseOperatorRepository.save(operator);
    }

    @Transactional
    public void reject(OperatorRequest operatorRequest) {
        operatorRequest.setStatus(RequestState.REJECTED);
        this.update(operatorRequest);
    }
}
