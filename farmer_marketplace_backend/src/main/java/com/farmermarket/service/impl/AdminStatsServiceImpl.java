package com.farmermarket.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmermarket.dto.response.AdminStatsDTO;
import com.farmermarket.entities.PaymentStatus;
import com.farmermarket.repository.CustomerRepository;
import com.farmermarket.repository.FarmerRepository;
import com.farmermarket.repository.OrderRepository;
import com.farmermarket.repository.PaymentRepository;
import com.farmermarket.repository.ProductRepository;
import com.farmermarket.service.AdminStatsService;

@Service
@Transactional(readOnly = true)
public class AdminStatsServiceImpl implements AdminStatsService {

    private final FarmerRepository farmerRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;

    public AdminStatsServiceImpl(
            FarmerRepository farmerRepository,
            CustomerRepository customerRepository,
            OrderRepository orderRepository,
            PaymentRepository paymentRepository,
            ProductRepository productRepository) {

        this.farmerRepository = farmerRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.productRepository = productRepository;
    }

    @Override
    public AdminStatsDTO getStats() {

        AdminStatsDTO dto = new AdminStatsDTO();

        dto.setTotalFarmers(farmerRepository.count());
        dto.setApprovedFarmers(
                farmerRepository.countByApprovedTrue()
        );

        dto.setTotalCustomers(customerRepository.count());
        dto.setTotalOrders(orderRepository.count());
        dto.setTotalProducts(productRepository.count());

        // ✅ Only SUCCESS payments count as revenue
        Double revenue = paymentRepository
                .sumAmountByStatus(PaymentStatus.SUCCESS);

        dto.setTotalRevenue(revenue != null ? revenue : 0.0);

        return dto;
    }
}
