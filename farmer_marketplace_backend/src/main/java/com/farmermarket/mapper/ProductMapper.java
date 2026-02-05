package com.farmermarket.mapper;

import com.farmermarket.dto.response.ProductResponseDTO;
import com.farmermarket.entities.Product;

public class ProductMapper {

    public static ProductResponseDTO toDTO(Product product) {

        ProductResponseDTO dto = new ProductResponseDTO();

        dto.setId(product.getId());          // ✅ correct
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setActive(product.getActive());         // ✅ correct

        // Farmer mapping (flattened)
        dto.setFarmerId(product.getFarmer().getId());
        dto.setFarmerName(product.getFarmer().getName());

        return dto;
    }
}
