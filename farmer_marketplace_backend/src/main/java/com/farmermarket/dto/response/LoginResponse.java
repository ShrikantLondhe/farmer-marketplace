package com.farmermarket.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

    private String username;
    private String role;
    private String token;     // 🔐 REQUIRED for JWT
    private String message;
}
