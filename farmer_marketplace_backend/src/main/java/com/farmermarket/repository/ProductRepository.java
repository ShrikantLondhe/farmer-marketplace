package com.farmermarket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmermarket.entities.Farmer;
import com.farmermarket.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Get all active products
    List<Product> findByActiveTrue();

    // Get all active products of a specific farmer
    List<Product> findByFarmerAndActiveTrue(Farmer farmer);
    
    List<Product> findByFarmer(Farmer farmer);

}
