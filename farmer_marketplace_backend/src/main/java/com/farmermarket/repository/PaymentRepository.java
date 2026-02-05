package com.farmermarket.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.farmermarket.entities.Payment;
import com.farmermarket.entities.PaymentStatus;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(Long orderId);

    boolean existsByOrderId(Long orderId);
    
 Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
//    @Query("SELECT COALESCE(SUM(p.amount),0) FROM Payment p WHERE p.status='SUCCESS'")
//    double totalRevenue();
    
 @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = :status")
 Double sumAmountByStatus(@Param("status") PaymentStatus status);


}
