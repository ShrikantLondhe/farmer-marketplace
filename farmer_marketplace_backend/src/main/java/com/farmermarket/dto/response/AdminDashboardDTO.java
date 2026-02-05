package com.farmermarket.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDashboardDTO {

    private long totalCustomers;
    private long totalFarmers;
    private long totalOrders;
    private double totalRevenue;

    private long pendingOrders;
    private long deliveredOrders;
}
