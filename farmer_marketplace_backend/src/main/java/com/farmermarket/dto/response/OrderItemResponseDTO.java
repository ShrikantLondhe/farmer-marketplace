package com.farmermarket.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponseDTO {

    private Long productId;
    private String productName;
    private Double priceAtPurchase;
    private Integer quantity;
}
