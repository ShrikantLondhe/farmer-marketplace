package com.farmermarket.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends BaseEntity {

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double priceAtPurchase;

    // Many items belong to one order
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore   // 🔥 THIS LINE FIXES 500 ERROR
    private Order order;

    // Many items refer to one product
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
