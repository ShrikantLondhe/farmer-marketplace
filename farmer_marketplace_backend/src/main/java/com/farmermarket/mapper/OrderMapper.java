package com.farmermarket.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.farmermarket.dto.response.OrderItemResponseDTO;
import com.farmermarket.dto.response.OrderResponseDTO;
import com.farmermarket.entities.Order;
import com.farmermarket.entities.OrderItem;
import com.farmermarket.entities.Payment;

public class OrderMapper {

    // ✅ USED WHEN ITEMS ARE AVAILABLE
    public static OrderResponseDTO toOrderResponseDTO(
            Order order,
            List<OrderItem> items,
            Payment payment
    ) {

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCustomerId(order.getCustomer().getId());

        // ✅ Payment status (SAFE)
        if (payment != null) {
            dto.setPaymentStatus(payment.getStatus());
        }

        List<OrderItemResponseDTO> itemDTOs = items.stream()
                .map(item -> {
                    OrderItemResponseDTO i = new OrderItemResponseDTO();
                    i.setProductId(item.getProduct().getId());
                    i.setProductName(item.getProduct().getName());
                    i.setPriceAtPurchase(item.getPriceAtPurchase());
                    i.setQuantity(item.getQuantity());
                    return i;
                })
                .collect(Collectors.toList());

        dto.setItems(itemDTOs);
        return dto;
    }

    // ✅ LIGHTWEIGHT VERSION (NO ITEMS)
    public static OrderResponseDTO toResponseDTO(
            Order order,
            Payment payment
    ) {

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCustomerId(order.getCustomer().getId());

        if (payment != null) {
            dto.setPaymentStatus(payment.getStatus());
        }

        return dto;
    }
}
