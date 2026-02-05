package com.farmermarket.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.farmermarket.entities.OrderStatus;
import com.farmermarket.entities.PaymentStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponseDTO {

    private Long orderId;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private Double totalAmount;
    private Long customerId;
    private List<OrderItemResponseDTO> items;
 
    private PaymentStatus paymentStatus;

}
