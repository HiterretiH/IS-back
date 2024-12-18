package org.lab.model;

import jakarta.persistence.*;

@Entity
@Table(name = "types_and_operators")
public class TypesAndOperators {
    @Id
    @ManyToOne
    @JoinColumn(name = "product_type_id", nullable = false)
    private ProductType productType;

    @ManyToOne
    @JoinColumn(name = "operator_id", nullable = false)
    private User operator;

    // Getters and Setters
}
