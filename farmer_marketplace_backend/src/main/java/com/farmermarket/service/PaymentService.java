package com.farmermarket.service;

import java.util.List;

import com.farmermarket.dto.request.PaymentVerificationRequest;
import com.farmermarket.dto.response.PaymentResponseDTO;
import com.farmermarket.entities.Payment;

public interface PaymentService {

    Payment createPayment(Long orderId, String paymentMethod);

    
    Payment verifyPayment(PaymentVerificationRequest request);
    
    Payment markPaymentFailed(Long orderId);

    List<PaymentResponseDTO> getAllPayments();


}
