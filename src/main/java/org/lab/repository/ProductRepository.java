package org.lab.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.lab.model.Product;
import org.lab.model.ProductType;

import java.util.List;

@Stateless
public class ProductRepository extends GenericRepository<Product, Integer> {

    public ProductRepository() {
        super(Product.class);
    }

    public List<Product> findAllInQueue(int queueId) {
        Query query = entityManager.createNativeQuery(
                "SELECT * FROM find_all_in_queue(:queueId)", Product.class);
        query.setParameter("queueId", queueId);
        return query.getResultList();
    }

    public List<String> findWithPagination(int page, int size) {
        Query query = entityManager.createNativeQuery("SELECT * FROM get_products_paginated(:page, :size)");
        query.setParameter("page", page);
        query.setParameter("size", size);

        return query.getResultList();
    }


    public Integer count() {
        Query query = entityManager.createNativeQuery("SELECT * FROM get_products_count()");
        return (Integer) query.getSingleResult();
    }
}
