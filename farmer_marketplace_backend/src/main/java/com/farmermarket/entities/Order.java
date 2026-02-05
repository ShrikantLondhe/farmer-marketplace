package com.farmermarket.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {

    @Column(nullable = false)
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    private LocalDateTime orderDate;
}
