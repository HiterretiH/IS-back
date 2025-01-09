package org.lab.model;

import jakarta.persistence.*;

@Entity
@Table(name = "sorting_station")
public class SortingStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "management_center_id", nullable = false)
    private GoodsManagementCenter managementCenter;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
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
