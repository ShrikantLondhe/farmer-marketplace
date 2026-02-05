package com.farmermarket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmermarket.entities.Farmer;
import com.farmermarket.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);
    
    List<OrderItem> findByProductFarmer(Farmer farmer);

}
