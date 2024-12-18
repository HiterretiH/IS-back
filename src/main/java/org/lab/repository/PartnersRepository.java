package org.lab.repository;

import jakarta.ejb.Stateless;
import org.lab.model.Partners;

@Stateless
public class PartnersRepository extends GenericRepository<Partners, Integer> {
    public PartnersRepository() {
        super(Partners.class);
    }
}
