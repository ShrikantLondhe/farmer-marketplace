package com.farmermarket.dto.response;

import java.time.LocalDateTime;

import com.farmermarket.entities.PaymentStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponseDTO {

    private Long paymentId;
    private Double amount;
    private PaymentStatus status;
    private String paymentMethod;
    private LocalDateTime paymentDate;
    private Long orderId;
}
