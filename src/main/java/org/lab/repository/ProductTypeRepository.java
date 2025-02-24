package org.lab.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.lab.model.ProductType;

import java.util.List;

@Stateless
public class ProductTypeRepository extends GenericRepository<ProductType, Integer> {
    public ProductTypeRepository() {
        super(ProductType.class);
    }

    public List<ProductType> findWithPagination(int page, int size) {
        Query query = entityManager.createNativeQuery("SELECT * FROM get_product_types_paginated(:page, :size)");
        query.setParameter("page", page);
        query.setParameter("size", size);

        return query.getResultList();
    }

    public Integer count() {
        Query query = entityManager.createNativeQuery("SELECT * FROM get_product_types_count()");
        return (Integer) query.getSingleResult();
    }
}
