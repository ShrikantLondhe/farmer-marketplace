package com.farmermarket.mapper;

import com.farmermarket.dto.response.FarmerResponseDTO;
import com.farmermarket.entities.Farmer;

public class FarmerMapper {

    public static FarmerResponseDTO toDTO(Farmer farmer) {

        FarmerResponseDTO dto = new FarmerResponseDTO();
        dto.setId(farmer.getId());
        dto.setName(farmer.getName());
        dto.setEmail(farmer.getEmail());
        dto.setPhone(farmer.getPhone());
        dto.setAddress(farmer.getAddress());
        dto.setApproved(farmer.getApproved());

        return dto;
    }
}
