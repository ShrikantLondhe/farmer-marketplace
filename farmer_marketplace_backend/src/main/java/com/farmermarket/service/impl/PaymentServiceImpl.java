package com.farmermarket.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmermarket.dto.request.PaymentVerificationRequest;
import com.farmermarket.dto.response.PaymentResponseDTO;
import com.farmermarket.entities.Order;
import com.farmermarket.entities.OrderStatus;
import com.farmermarket.entities.Payment;
import com.farmermarket.entities.PaymentStatus;
import com.farmermarket.exception.BadRequestException;
import com.farmermarket.exception.ResourceNotFoundException;
import com.farmermarket.repository.OrderRepository;
import com.farmermarket.repository.PaymentRepository;
import com.farmermarket.service.PaymentService;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final RazorpayClient razorpayClient;

    @Value("${razorpay.key.secret}")
    private String razorpaySecret;

    @Value("${payment.test-mode:false}")
    private boolean testMode;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository,
            RazorpayClient razorpayClient) {

        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.razorpayClient = razorpayClient;
    }

    // ================= CREATE PAYMENT =================
    @Override
    public Payment createPayment(Long orderId, String paymentMethod) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new BadRequestException(
                    "Payment can only be created for CREATED orders");
        }

        if (paymentRepository.existsByOrderId(orderId)) {
            throw new BadRequestException("Payment already exists for this order");
        }

        try {
            JSONObject options = new JSONObject();
            options.put("amount", order.getTotalAmount() * 100); // paise
            options.put("currency", "INR");
            options.put("receipt", "order_rcpt_" + orderId);

            com.razorpay.Order razorpayOrder =
                    razorpayClient.orders.create(options);

            Payment payment = new Payment();
            payment.setOrder(order);
            payment.setAmount(order.getTotalAmount());
            payment.setPaymentMethod(paymentMethod);
            payment.setStatus(PaymentStatus.PENDING);
            payment.setCreatedAt(LocalDateTime.now());
            payment.setRazorpayOrderId(razorpayOrder.get("id"));

            return paymentRepository.save(payment);

        } catch (Exception e) {
            throw new RuntimeException("Razorpay order creation failed", e);
        }
    }

    // ================= VERIFY PAYMENT =================
    @Override
    public Payment verifyPayment(PaymentVerificationRequest request) {

        Payment payment = paymentRepository
                .findByOrderId(request.getOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found"));

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            throw new BadRequestException("Payment already verified");
        }

        String payload =
                request.getRazorpayOrderId() + "|" +
                request.getRazorpayPaymentId();

        try {
            boolean isValid = true;

            if (!testMode) {
                isValid = Utils.verifySignature(
                        payload,
                        request.getRazorpaySignature(),
                        razorpaySecret
                );
            }

            if (!isValid) {
                throw new BadRequestException("Payment verification failed");
            }

            // ✅ Update payment
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
            payment.setRazorpayOrderId(request.getRazorpayOrderId());
            payment.setCreatedAt(LocalDateTime.now());

            // ❗ IMPORTANT:
            // Order stays CREATED → Farmer confirms later
            paymentRepository.save(payment);

            return payment;

        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Razorpay verification error", e);
        }
    }

    // ================= MARK PAYMENT FAILED =================
    @Override
    public Payment markPaymentFailed(Long orderId) {

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found"));

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            throw new BadRequestException(
                    "Payment already successful, cannot mark as failed");
        }

        payment.setStatus(PaymentStatus.FAILED);
        payment.setCreatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    // ================= ADMIN VIEW PAYMENTS =================
    @Override
    public List<PaymentResponseDTO> getAllPayments() {

        return paymentRepository.findAll()
                .stream()
                .map(payment -> {
                    PaymentResponseDTO dto = new PaymentResponseDTO();
                    dto.setPaymentId(payment.getId());
                    dto.setOrderId(payment.getOrder().getId());
                    dto.setAmount(payment.getAmount());
                    dto.setStatus(payment.getStatus());
                    dto.setPaymentMethod(payment.getPaymentMethod());
                    dto.setPaymentDate(payment.getPaymentDate());
                    return dto;
                })
                .toList();
    }
}
