package com.farmermarket.service;

import java.util.List;

import com.farmermarket.dto.request.RegisterFarmerRequest;
import com.farmermarket.entities.Farmer;

public interface FarmerService {

    Farmer registerFarmer(RegisterFarmerRequest request);

    Farmer getFarmerById(Long id);

    List<Farmer> getAllFarmers();

    Farmer approveFarmer(Long id);
    
    Farmer getFarmerByEmail(String email);

}
