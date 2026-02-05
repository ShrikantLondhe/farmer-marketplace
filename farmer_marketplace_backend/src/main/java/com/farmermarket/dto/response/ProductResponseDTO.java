package com.farmermarket.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDTO {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Boolean active;

    // Farmer info (flattened – best practice)
    private Long farmerId;
    private String farmerName;
}
