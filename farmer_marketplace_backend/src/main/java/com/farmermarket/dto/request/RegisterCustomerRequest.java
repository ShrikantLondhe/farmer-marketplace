package com.farmermarket.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterCustomerRequest {

    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
}
