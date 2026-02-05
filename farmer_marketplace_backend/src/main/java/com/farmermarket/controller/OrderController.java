package com.farmermarket.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmermarket.dto.request.OrderRequest;
import com.farmermarket.dto.response.OrderResponseDTO;
import com.farmermarket.entities.Order;
import com.farmermarket.entities.OrderItem;
import com.farmermarket.entities.OrderStatus;
import com.farmermarket.entities.Payment;
import com.farmermarket.mapper.OrderMapper;
import com.farmermarket.repository.OrderItemRepository;
import com.farmermarket.repository.PaymentRepository;
import com.farmermarket.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;

    public OrderController(OrderService orderService,
                           OrderItemRepository orderItemRepository,
                           PaymentRepository paymentRepository) {
        this.orderService = orderService;
        this.orderItemRepository = orderItemRepository;
        this.paymentRepository = paymentRepository;
    }

    // ================= CREATE ORDER =================
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(
            @Valid @RequestBody OrderRequest request,
            Authentication authentication) {

        String username = authentication.getName();

        Order order = orderService.createOrder(
                username,
                request.getProductIds(),
                request.getQuantities()
        );

        List<OrderItem> items =
                orderItemRepository.findByOrderId(order.getId());

        Payment payment =
                paymentRepository.findByOrderId(order.getId()).orElse(null);

        return ResponseEntity.ok(
                OrderMapper.toOrderResponseDTO(order, items, payment)
        );
    }

    // ================= CUSTOMER ORDERS =================
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/my")
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders(
            Authentication authentication) {

        String username = authentication.getName();
        List<Order> orders = orderService.getMyOrders(username);

        List<OrderResponseDTO> response = orders.stream()
                .map(order -> {
                    List<OrderItem> items =
                            orderItemRepository.findByOrderId(order.getId());

                    Payment payment =
                            paymentRepository.findByOrderId(order.getId()).orElse(null);

                    return OrderMapper.toOrderResponseDTO(order, items, payment);
                })
                .toList();

        return ResponseEntity.ok(response);
    }

    // ================= ADMIN ORDERS =================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrdersForAdmin() {

        List<Order> orders = orderService.getAllOrders();

        List<OrderResponseDTO> response = orders.stream()
                .map(order -> {
                    List<OrderItem> items =
                            orderItemRepository.findByOrderId(order.getId());

                    Payment payment =
                            paymentRepository.findByOrderId(order.getId()).orElse(null);

                    return OrderMapper.toOrderResponseDTO(order, items, payment);
                })
                .toList();

        return ResponseEntity.ok(response);
    }

    // ================= FARMER ORDERS =================
    @PreAuthorize("hasRole('FARMER')")
    @GetMapping("/farmer")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersForFarmer(
            Authentication authentication) {

        String email = authentication.getName();
        return ResponseEntity.ok(orderService.getOrdersForFarmer(email));
    }

    // ================= UPDATE STATUS (ADMIN) =================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {

        Order order = orderService.updateOrderStatus(orderId, status);

        List<OrderItem> items =
                orderItemRepository.findByOrderId(order.getId());

        Payment payment =
                paymentRepository.findByOrderId(order.getId()).orElse(null);

        return ResponseEntity.ok(
                OrderMapper.toOrderResponseDTO(order, items, payment)
        );
    }

    // ================= FARMER CONFIRM =================
    @PreAuthorize("hasRole('FARMER')")
    @PutMapping("/{orderId}/confirm")
    public ResponseEntity<OrderResponseDTO> confirmOrder(
            @PathVariable Long orderId,
            Authentication authentication) {

        String email = authentication.getName();

        Order order = orderService.confirmOrder(orderId, email);

        List<OrderItem> items =
                orderItemRepository.findByOrderId(order.getId());

        Payment payment =
                paymentRepository.findByOrderId(order.getId()).orElse(null);

        return ResponseEntity.ok(
                OrderMapper.toOrderResponseDTO(order, items, payment)
        );
    }

    // ================= CUSTOMER CANCEL =================
    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponseDTO> cancelOrder(
            @PathVariable Long orderId,
            Authentication authentication) {

        String username = authentication.getName();

        Order order = orderService.cancelOrder(orderId, username);

        List<OrderItem> items =
                orderItemRepository.findByOrderId(order.getId());

        Payment payment =
                paymentRepository.findByOrderId(order.getId()).orElse(null);

        return ResponseEntity.ok(
                OrderMapper.toOrderResponseDTO(order, items, payment)
        );
    }
    
    
    @PreAuthorize("hasRole('FARMER')")
    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<OrderResponseDTO> deliverOrder(
            @PathVariable Long orderId,
            Authentication authentication) {

        Order order = orderService.updateOrderStatus(
                orderId,
                OrderStatus.DELIVERED
        );

        List<OrderItem> items =
                orderItemRepository.findByOrderId(order.getId());

        Payment payment =
                paymentRepository.findByOrderId(order.getId()).orElse(null);

        return ResponseEntity.ok(
                OrderMapper.toOrderResponseDTO(order, items, payment)
        );
    }


}
