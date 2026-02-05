package com.farmermarket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmermarket.entities.Order;
import com.farmermarket.entities.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByCustomerId(Long customerId);
	long countByStatus(OrderStatus status);
	

}
