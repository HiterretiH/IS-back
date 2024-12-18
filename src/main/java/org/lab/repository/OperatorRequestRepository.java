package org.lab.repository;

import jakarta.ejb.Stateless;
import org.lab.model.OperatorRequest;

@Stateless
public class OperatorRequestRepository extends GenericRepository<OperatorRequest, Integer> {
    public OperatorRequestRepository() {
        super(OperatorRequest.class);
    }
}
