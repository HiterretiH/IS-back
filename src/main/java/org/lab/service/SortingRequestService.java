package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.lab.model.*;
import org.lab.repository.SortingRequestRepository;
import org.lab.repository.WarehouseOperatorRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Transactional
    public void reject(SortingRequest sortingRequest) {
        List<SortingRequest> requests = sortingRequestRepository.findAll();

        Map<User, Integer> operatorRequestCountMap = new HashMap<>();

        for (SortingRequest request : requests) {
            User operator = request.getOperator();
            if (operator.getRole() != Role.MANAGER)
                operatorRequestCountMap.put(operator, operatorRequestCountMap.getOrDefault(operator, 0) + 1);
        }

        User leastBusyOperator = null;
        int minRequests = Integer.MAX_VALUE;

        for (Map.Entry<User, Integer> entry : operatorRequestCountMap.entrySet()) {
            if (entry.getValue() < minRequests) {
                minRequests = entry.getValue();
                leastBusyOperator = entry.getKey();
            }
        }

        sortingRequest.setOperator(leastBusyOperator);
        sortingRequestRepository.update(sortingRequest);
    }
}
