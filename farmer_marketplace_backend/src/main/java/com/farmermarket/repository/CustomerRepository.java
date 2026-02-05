package com.farmermarket.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmermarket.entities.Customer;
import com.farmermarket.security.User;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	Optional<Customer> findByUser(User user);

}
