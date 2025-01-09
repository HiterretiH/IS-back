package org.lab.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "queue")
public class Queue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotNull
    private Integer capacity;

    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    @NotNull
    private SortingStation sortingStation;

    public SortingStation getSortingStation() {
        return sortingStation;
    }

    public void setSortingStation(SortingStation sortingStation) {
        this.sortingStation = sortingStation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
