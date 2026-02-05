package com.farmermarket.mapper;

import com.farmermarket.dto.response.PaymentResponseDTO;
import com.farmermarket.entities.Payment;

public class PaymentMapper {

    public static PaymentResponseDTO toDTO(Payment payment) {

        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setPaymentId(payment.getId());
        dto.setAmount(payment.getAmount());
        dto.setStatus(payment.getStatus());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setOrderId(payment.getOrder().getId());

        return dto;
    }
}
