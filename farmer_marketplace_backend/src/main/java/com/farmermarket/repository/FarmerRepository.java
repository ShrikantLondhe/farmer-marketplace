package com.farmermarket.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmermarket.entities.Farmer;

public interface FarmerRepository extends JpaRepository<Farmer, Long> {
	Optional<Farmer> findByEmail(String email);
	long countByApprovedTrue();

}
