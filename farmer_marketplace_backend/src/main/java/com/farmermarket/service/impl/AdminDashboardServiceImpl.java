package com.farmermarket.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmermarket.dto.response.AdminDashboardDTO;
import com.farmermarket.entities.OrderStatus;
import com.farmermarket.entities.PaymentStatus;
import com.farmermarket.repository.CustomerRepository;
import com.farmermarket.repository.FarmerRepository;
import com.farmermarket.repository.OrderRepository;
import com.farmermarket.repository.PaymentRepository;
import com.farmermarket.service.AdminDashboardService;

@Service
@Transactional(readOnly = true)
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final CustomerRepository customerRepo;
    private final FarmerRepository farmerRepo;
    private final OrderRepository orderRepo;
    private final PaymentRepository paymentRepo;

    public AdminDashboardServiceImpl(
            CustomerRepository customerRepo,
            FarmerRepository farmerRepo,
            OrderRepository orderRepo,
            PaymentRepository paymentRepo) {

        this.customerRepo = customerRepo;
        this.farmerRepo = farmerRepo;
        this.orderRepo = orderRepo;
        this.paymentRepo = paymentRepo;
    }

    @Override
    public AdminDashboardDTO getDashboardData() {

        AdminDashboardDTO dto = new AdminDashboardDTO();

        dto.setTotalCustomers(customerRepo.count());
        dto.setTotalFarmers(farmerRepo.count());
        dto.setTotalOrders(orderRepo.count());

        Double revenue =
                paymentRepo.sumAmountByStatus(PaymentStatus.SUCCESS);

        dto.setTotalRevenue(revenue != null ? revenue : 0.0);

        dto.setPendingOrders(
                orderRepo.countByStatus(OrderStatus.CREATED)
        );

        dto.setDeliveredOrders(
                orderRepo.countByStatus(OrderStatus.DELIVERED)
        );

        return dto;
    }
}
