package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.lab.model.*;
import org.lab.repository.ProductRepository;
import org.lab.repository.QueueRepository;
import org.lab.repository.SortingStationRepository;
import org.lab.utils.PaginatedResponse;

import java.util.List;

@Stateless
public class ProductService {

    @Inject
    private ProductRepository productRepository;

    @Inject
    private SortingStationRepository sortingStationRepository;

    @Inject
    private QueueRepository queueRepository;

    public PaginatedResponse<String> getAll(int page, int size) {
        List<String> data = productRepository.findWithPagination(page, size);
        int count = productRepository.count();
        return new PaginatedResponse<>(data, count);
    }

    public Product getById(int id) {
        return productRepository.findById(id);
    }

    public void create(Product product) {
        Product newProduct = new Product();
        newProduct.setProductType(product.getProductType());
        newProduct.setLocation(product.getLocation());
        newProduct.setSupplier(product.getSupplier());
        newProduct.setCustomer(product.getCustomer());
        newProduct.setQueue(product.getQueue());
        newProduct.setName(product.getName());
        newProduct.setDescription(product.getDescription());
        newProduct.setExpirationDate(product.getExpirationDate());
        newProduct.setProductState(product.getProductState());
        newProduct.setPriority(product.getPriority());
        newProduct.setProductState(ProductState.PENDING);

        productRepository.save(newProduct);
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
            queue.setCapacity(10); // Default capacity
            queue.setSortingStation(sortingStation);
            queueRepository.update(queue);
        }

        // Check if the queue has enough capacity
        int productsInQueue = productRepository.countByQueue(queue.getId());
        if (productsInQueue >= queue.getCapacity()) {
            throw new IllegalStateException("Queue capacity exceeded. Cannot add product to the queue.");
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

            int productsInQueue = productRepository.countByQueue(q.getId());
            if (productsInQueue < q.getCapacity() && countHigherPriority < minCount) {
                minCount = (int) countHigherPriority;
                optQueue = q;
            }
        }

        return optQueue;
    }

    @Transactional
    public void setPriority(Product product, int priority) {
        product.setPriority(priority);
        this.update(product);
    }
}
