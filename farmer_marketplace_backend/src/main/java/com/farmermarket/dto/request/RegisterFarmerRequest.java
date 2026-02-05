package com.farmermarket.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterFarmerRequest {

    private String name;
    private String email;
    private String phone;
    private String address;
    private String password;
}
