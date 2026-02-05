package com.farmermarket.mapper;

import com.farmermarket.dto.response.CustomerResponseDTO;
import com.farmermarket.entities.Customer;

public class CustomerMapper {

    public static CustomerResponseDTO toDTO(Customer customer) {

        CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        dto.setAddress(customer.getAddress());

        return dto;
    }
}
