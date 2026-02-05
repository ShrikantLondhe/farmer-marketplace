package com.farmermarket.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
}
