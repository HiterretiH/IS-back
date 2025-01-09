package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.lab.model.Product;
import org.lab.model.ProductState;
import org.lab.model.Queue;
import org.lab.model.SortingStation;
import org.lab.repository.ProductRepository;
import org.lab.repository.QueueRepository;
import org.lab.repository.SortingStationRepository;

import java.util.List;

@Stateless
public class ProductService {

    @Inject
    private ProductRepository productRepository;

    @Inject
    private SortingStationRepository sortingStationRepository;

    @Inject
    private QueueRepository queueRepository;

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getById(int id) {
        return productRepository.findById(id);
    }

    public void create(Product product) {
        productRepository.save(product);
    }

    public void update(Product product) {
        productRepository.update(product);
    }

    public void delete(Product product) {
        productRepository.delete(product.getId());
    }


    @Transactional
    public void dispose(Product product) {
        product.setProductState(ProductState.DISPOSED);
        this.update(product);
    }

    @Transactional
    public void sortToShip(Product product, SortingStation sortingStation) {
        product.setProductState(ProductState.SORTING_TO_SHIP);
        this.update(product);
        putInQueue(product, sortingStation);
    }


    @Transactional
    public void sortToStore(Product product, SortingStation sortingStation) {
        product.setProductState(ProductState.SORTING_TO_STORE);
        this.update(product);
        putInQueue(product, sortingStation);
    }

    @Transactional
    public void putInQueue(Product product, SortingStation sortingStation) {
        List<Queue> queues = queueRepository.findBySortingStation(sortingStation);

        Queue queue = findOptimalQueue(product, queues);

        if (queue == null) {
            queue = new Queue();
            queue.setSortingStation(sortingStation);
            queueRepository.update(queue);
        }

        product.setQueue(queue);
        this.update(product);
    }

    private Queue findOptimalQueue(Product currentProduct, List<Queue> queues) {
        Queue optQueue = null;
        int minCount = Integer.MAX_VALUE;

        for (Queue q : queues) {
            long countHigherPriority = productRepository.findAllInQueue(q.getId()).stream()
                    .filter(p -> p.getPriority() > currentProduct.getPriority())
                    .count();

            if (countHigherPriority < minCount) {
                minCount = (int) countHigherPriority;
                optQueue = q;
            }
        }

        return optQueue;
    }

    // Изменить приоритет, используемый при сортировке
    @Transactional
    public void setPriority(Product product, int priority) {
        product.setPriority(priority);
        this.update(product);
    }
}
