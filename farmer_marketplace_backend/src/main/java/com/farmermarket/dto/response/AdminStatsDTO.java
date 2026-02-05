package com.farmermarket.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminStatsDTO {

    private long totalFarmers;
    private long approvedFarmers;
    private long totalCustomers;
    private long totalOrders;
    private double totalRevenue;
    private long totalProducts;
}
