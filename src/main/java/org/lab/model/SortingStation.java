package org.lab.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "sorting_station")
public class SortingStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "management_center_id", nullable = false)
    @NotNull
    private GoodsManagementCenter managementCenter;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    @NotNull
    private Location location;

    @Column(nullable = false)
    @NotNull
    private Integer capacity;

    @Column(nullable = false)
    @NotNull
    private Integer sortTimeSeconds;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GoodsManagementCenter getManagementCenter() {
        return managementCenter;
    }

    public void setManagementCenter(GoodsManagementCenter managementCenter) {
        this.managementCenter = managementCenter;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getSortTimeSeconds() {
        return sortTimeSeconds;
    }

    public void setSortTimeSeconds(Integer performance) {
        this.sortTimeSeconds = performance;
    }
}
