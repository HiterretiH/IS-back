package org.lab.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import org.lab.model.Product;

import java.util.List;

@Stateless
public class ProductRepository extends GenericRepository<Product, Integer> {
    public ProductRepository() {
        super(Product.class);
    }

    public List<Product> findAllInQueue(int queueId) {
        String jpql = "SELECT p FROM Product p WHERE p.queue.id = :queueId";
        TypedQuery<Product> query = entityManager.createQuery(jpql, Product.class);
        query.setParameter("queueId", queueId);
        return query.getResultList();
    }
}
