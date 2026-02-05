package com.farmermarket.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmermarket.dto.request.PaymentVerificationRequest;
import com.farmermarket.dto.response.PaymentResponseDTO;
import com.farmermarket.entities.Payment;
import com.farmermarket.mapper.PaymentMapper;
import com.farmermarket.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(
            @RequestParam Long orderId,
            @RequestParam String paymentMethod) {

        Payment payment = paymentService.createPayment(orderId, paymentMethod);
        return ResponseEntity.ok(PaymentMapper.toDTO(payment));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/verify")
    public ResponseEntity<PaymentResponseDTO> verifyPayment(
            @RequestBody PaymentVerificationRequest request) {

        Payment payment = paymentService.verifyPayment(request);
        return ResponseEntity.ok(PaymentMapper.toDTO(payment));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/fail")
    public ResponseEntity<PaymentResponseDTO> markPaymentFailed(
            @RequestParam Long orderId) {

        Payment payment = paymentService.markPaymentFailed(orderId);
        return ResponseEntity.ok(PaymentMapper.toDTO(payment));
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }
}
