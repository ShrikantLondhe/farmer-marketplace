package com.farmermarket.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmermarket.entities.StockDetails;

public interface StockDetailsRepository extends JpaRepository<StockDetails, Long> {

    Optional<StockDetails> findByProductId(Long productId);
}
