package org.lab.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.Query;
import org.lab.model.Partners;

import java.util.List;

@Stateless
public class PartnersRepository extends GenericRepository<Partners, Integer> {
    public PartnersRepository() {
        super(Partners.class);
    }

    public List<String> findWithPagination(int page, int size) {
        Query query = entityManager.createNativeQuery("SELECT * FROM get_partners_paginated(:page, :size)");
        query.setParameter("page", page);
        query.setParameter("size", size);

        return query.getResultList();
    }


    public Integer count() {
        Query query = entityManager.createNativeQuery("SELECT * FROM get_partners_count()");
        return (Integer) query.getSingleResult();
    }
}
