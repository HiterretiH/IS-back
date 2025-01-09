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
import java.util.Comparator;

@Stateless
public class SortingStationService {
    @Inject
    private SortingStationRepository sortingStationRepository;

    @Inject
    private QueueRepository queueRepository;

    @Inject
    private ProductRepository productRepository;

    public List<SortingStation> getAll() {
        return sortingStationRepository.findAll();
    }

    public SortingStation getById(int id) {
        return sortingStationRepository.findById(id);
    }

    public void create(SortingStation sortingStation) {
        sortingStationRepository.save(sortingStation);
    }

    public void delete(SortingStation sortingStation) {
        sortingStationRepository.delete(sortingStation.getId());
    }

    @Transactional
    public void simulateSort(SortingStation sortingStation) {
        List<Queue> queues = queueRepository.findBySortingStation(sortingStation);

        for (Queue queue : queues) {
            List<Product> productsInQueue = productRepository.findAllInQueue(queue.getId());
            productsInQueue.sort(Comparator.comparingInt(Product::getPriority).reversed());

            for (Product product : productsInQueue) {
                simulateSortingForProduct(product, sortingStation);
            }
        }
    }

    private void simulateSortingForProduct(Product product, SortingStation sortingStation) {
        int timeDelaySeconds = sortingStation.getSortTimeSeconds();
        try {
            System.out.println("Sorting product: " + product.getName() + " (Delay: " + timeDelaySeconds + " seconds)");
            Thread.sleep(timeDelaySeconds * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        product.setProductState(
                product.getProductState() == ProductState.SORTING_TO_SHIP ?
                ProductState.SHIPPED :
                ProductState.SORTING_TO_STORE
        );

        productRepository.update(product);
        System.out.println("Finished sorting product: " + product.getName());
    }
}
