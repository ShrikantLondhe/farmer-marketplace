package com.farmermarket.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {

//    @NotNull
//    private Long customerId;

    @NotEmpty
    private List<Long> productIds;

    @NotEmpty
    private List<Integer> quantities;
}
