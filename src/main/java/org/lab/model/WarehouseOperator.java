package org.lab.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "warehouse_operator")
public class WarehouseOperator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "app_user_id", nullable = false)
    @NotNull
    private User appUser;

    @ManyToOne
    @JoinColumn(name = "product_type_id", nullable = false
    )
    private ProductType productType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getAppUser() {
        return appUser;
    }

    public ProductType getProductType() {
        return productType;
    }
    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public void setAppUser(User appUser) {
        this.appUser = appUser;
    }
}
