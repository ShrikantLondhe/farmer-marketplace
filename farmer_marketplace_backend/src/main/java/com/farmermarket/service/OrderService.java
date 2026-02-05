package com.farmermarket.service;

import java.util.List;

import com.farmermarket.dto.response.OrderResponseDTO;
import com.farmermarket.entities.Order;
import com.farmermarket.entities.OrderStatus;

public interface OrderService {

	Order createOrder(String username,
            List<Long> productIds,
            List<Integer> quantities);

	Order getOrderById(Long orderId, String username);


    List<Order> getAllOrders();

    List<Order> getMyOrders(String username);

    Order updateOrderStatus(Long orderId, OrderStatus status);
    
    List<OrderResponseDTO> getOrdersByCustomer(Long customerId);
    
    List<OrderResponseDTO> getOrdersForFarmer(String farmerEmail);
    
    Order confirmOrder(Long orderId, String farmerEmail);

    Order cancelOrder(Long orderId, String username);


}
