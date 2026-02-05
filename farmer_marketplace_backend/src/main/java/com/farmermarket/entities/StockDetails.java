package com.farmermarket.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "stock_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockDetails extends BaseEntity {

    @Column(nullable = false)
    private Integer quantity;

    // One stock record belongs to one product
    @OneToOne
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;
}
