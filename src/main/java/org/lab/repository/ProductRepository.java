package org.lab.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.lab.model.Product;

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
}
