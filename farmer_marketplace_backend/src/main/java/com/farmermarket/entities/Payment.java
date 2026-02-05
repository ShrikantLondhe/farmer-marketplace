package com.farmermarket.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(nullable = false)
    private String paymentMethod;   // CARD, UPI, NET_BANKING

    private LocalDateTime paymentDate;

    @Column(unique = true)
    private String razorpayOrderId;

    private String razorpayPaymentId;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;
}
