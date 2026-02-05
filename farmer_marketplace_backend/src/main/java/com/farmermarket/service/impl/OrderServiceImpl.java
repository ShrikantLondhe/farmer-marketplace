package com.farmermarket.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmermarket.dto.response.OrderResponseDTO;
import com.farmermarket.entities.Customer;
import com.farmermarket.entities.Farmer;
import com.farmermarket.entities.Order;
import com.farmermarket.entities.OrderItem;
import com.farmermarket.entities.OrderStatus;
import com.farmermarket.entities.Payment;
import com.farmermarket.entities.PaymentStatus;
import com.farmermarket.entities.Product;
import com.farmermarket.exception.BadRequestException;
import com.farmermarket.exception.ResourceNotFoundException;
import com.farmermarket.mapper.OrderMapper;
import com.farmermarket.repository.CustomerRepository;
import com.farmermarket.repository.FarmerRepository;
import com.farmermarket.repository.OrderItemRepository;
import com.farmermarket.repository.OrderRepository;
import com.farmermarket.repository.PaymentRepository;
import com.farmermarket.repository.ProductRepository;
import com.farmermarket.security.Role;
import com.farmermarket.security.User;
import com.farmermarket.security.UserRepository;
import com.farmermarket.service.OrderService;
import com.farmermarket.service.StockDetailsService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger log =
            LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final StockDetailsService stockService;
    private final UserRepository userRepository;
    private final FarmerRepository farmerRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;


    public OrderServiceImpl(
            OrderRepository orderRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository,
            StockDetailsService stockService,
            UserRepository userRepository,
            FarmerRepository farmerRepository,
            OrderItemRepository orderItemRepository,
            PaymentRepository paymentRepository) {

        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.stockService = stockService;
        this.userRepository = userRepository;
        this.farmerRepository = farmerRepository;
        this.orderItemRepository = orderItemRepository;
        this.paymentRepository = paymentRepository;
    }


    // ================= CREATE ORDER =================
    @Override
    public Order createOrder(String username,
                             List<Long> productIds,
                             List<Integer> quantities) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        if (productIds.size() != quantities.size()) {
            throw new BadRequestException("Product & quantity mismatch");
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.CREATED);
        order.setOrderDate(LocalDateTime.now());

        double totalAmount = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (int i = 0; i < productIds.size(); i++) {

            Product product = productRepository.findById(productIds.get(i))
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Product not found"));

            int qty = quantities.get(i);

            stockService.reduceStock(product.getId(), qty);

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(qty);
            item.setPriceAtPurchase(product.getPrice());

            totalAmount += product.getPrice() * qty;
            orderItems.add(item);
        }

        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);

        return orderRepository.save(order);
    }

    // ================= CUSTOMER =================
    @Override
    public List<OrderResponseDTO> getOrdersByCustomer(Long customerId) {

        return orderRepository.findByCustomerId(customerId)
                .stream()
                .map(order -> {
                    var payment = paymentRepository
                            .findByOrderId(order.getId())
                            .orElse(null);
                    return OrderMapper.toResponseDTO(order, payment);
                })
                .collect(Collectors.toList());
    }


    @Override
    public List<Order> getMyOrders(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (user.getRole() != Role.CUSTOMER) {
            throw new BadRequestException("Only customers can view their orders");
        }

        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        return orderRepository.findByCustomerId(customer.getId());
    }

    // ================= ORDER ACCESS =================
    @Override
    public Order getOrderById(Long orderId, String username) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (user.getRole() == Role.ADMIN) {
            return order;
        }

        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        if (!order.getCustomer().getId().equals(customer.getId())) {
            throw new BadRequestException("Access denied");
        }

        return order;
    }

    // ================= ADMIN =================
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        OrderStatus current = order.getStatus();

        if (current == OrderStatus.DELIVERED || current == OrderStatus.CANCELLED) {
            throw new BadRequestException("Order cannot be updated");
        }

        if (current == OrderStatus.CREATED &&
                !(newStatus == OrderStatus.CONFIRMED ||
                  newStatus == OrderStatus.CANCELLED)) {
            throw new BadRequestException("Invalid transition from CREATED");
        }

        if (current == OrderStatus.CONFIRMED &&
                newStatus != OrderStatus.DELIVERED) {
            throw new BadRequestException("Only DELIVERED allowed");
        }
        Payment payment = paymentRepository
                .findByOrderId(orderId)
                .orElse(null);

        if (payment != null &&
            payment.getStatus() != PaymentStatus.SUCCESS &&
            newStatus != OrderStatus.CANCELLED) {

            throw new BadRequestException(
                "Order cannot be updated until payment is successful"
            );
        }


        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    // ================= FARMER =================
    @Override
    public List<OrderResponseDTO> getOrdersForFarmer(String farmerEmail) {

        Farmer farmer = farmerRepository.findByEmail(farmerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));

        return orderItemRepository.findByProductFarmer(farmer)
                .stream()
                .map(OrderItem::getOrder)
                .distinct()
                .map(order -> {
                    List<OrderItem> items =
                            orderItemRepository.findByOrderId(order.getId());

                    Payment payment =
                            paymentRepository.findByOrderId(order.getId()).orElse(null);

                    return OrderMapper.toOrderResponseDTO(order, items, payment);
                })
                .toList();
    }
 
    @Override
    public Order confirmOrder(Long orderId, String farmerEmail) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new BadRequestException("Only CREATED orders can be confirmed");
        }

        Farmer farmer = farmerRepository.findByEmail(farmerEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Farmer not found"));

        boolean belongsToFarmer = order.getOrderItems().stream()
                .anyMatch(item ->
                        item.getProduct().getFarmer().getId()
                                .equals(farmer.getId()));

        if (!belongsToFarmer) {
            throw new BadRequestException("Not your order");
        }
        Payment payment = paymentRepository
                .findByOrderId(orderId)
                .orElseThrow(() ->
                        new BadRequestException("Payment not completed"));

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new BadRequestException(
                "Order cannot be confirmed until payment is successful"
            );
        }


        order.setStatus(OrderStatus.CONFIRMED);
        return orderRepository.save(order);
    }

    // ================= CANCEL =================
    @Override
    public Order cancelOrder(Long orderId, String username) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (user.getRole() != Role.CUSTOMER) {
            throw new BadRequestException("Only customers can cancel");
        }

        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        if (!order.getCustomer().getId().equals(customer.getId())) {
            throw new BadRequestException("Not your order");
        }

        Payment payment = paymentRepository
                .findByOrderId(orderId)
                .orElse(null);

        // 🔒 BLOCK cancel after payment success
        if (payment != null && payment.getStatus() == PaymentStatus.SUCCESS) {
            throw new BadRequestException("Paid order cannot be cancelled");
        }

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new BadRequestException("Only CREATED orders can be cancelled");
        }

        // restore stock
        for (OrderItem item : order.getOrderItems()) {
            stockService.addStock(
                    item.getProduct().getId(),
                    item.getQuantity()
            );
        }

        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

}
