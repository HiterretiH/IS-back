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
import java.util.Comparator;

@Stateless
public class SortingStationService {
    @Inject
    private SortingStationRepository sortingStationRepository;

    @Inject
    private QueueRepository queueRepository;

    @Inject
    private ProductRepository productRepository;

    public PaginatedResponse<String> getAll(int page, int size) {
        List<String> data = sortingStationRepository.findWithPagination(page, size);
        int count = sortingStationRepository.count();
        return new PaginatedResponse<>(data, count);
    }

    public SortingStation getById(int id) {
        return sortingStationRepository.findById(id);
    }

    public void create(SortingStation sortingStation) {
        SortingStation newSortingStation = new SortingStation();
        newSortingStation.setWarehouse(sortingStation.getWarehouse());
        newSortingStation.setLocation(sortingStation.getLocation());
        newSortingStation.setCapacity(sortingStation.getCapacity());
        newSortingStation.setSortTimeSeconds(sortingStation.getSortTimeSeconds());

        sortingStationRepository.save(newSortingStation);
    }

    public void update(SortingStation sortingStation) {
        sortingStationRepository.update(sortingStation);
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
            Thread.sleep(timeDelaySeconds * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        product.setProductState(
                product.getProductState() == ProductState.SORTING_TO_SHIP ?
                ProductState.SHIPPED :
                ProductState.STORED
        );
        product.setQueue(null);

        productRepository.update(product);
    }
}
