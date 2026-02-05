package com.farmermarket.mapper;

import com.farmermarket.dto.response.StockResponseDTO;
import com.farmermarket.entities.StockDetails;

public class StockMapper {

    public static StockResponseDTO toDTO(StockDetails stock) {

        StockResponseDTO dto = new StockResponseDTO();
        dto.setStockId(stock.getId());
        dto.setProductId(stock.getProduct().getId());
        dto.setQuantity(stock.getQuantity());

        return dto;
    }
}
