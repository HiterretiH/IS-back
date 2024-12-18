package org.lab.repository;

import jakarta.ejb.Stateless;
import org.lab.model.SortingRequest;

@Stateless
public class SortingRequestRepository extends GenericRepository<SortingRequest, Integer> {
    public SortingRequestRepository() {
        super(SortingRequest.class);
    }
}
