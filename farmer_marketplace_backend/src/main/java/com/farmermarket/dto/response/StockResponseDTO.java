package com.farmermarket.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockResponseDTO {

    private Long stockId;
    private Long productId;
    private Integer quantity;
}
