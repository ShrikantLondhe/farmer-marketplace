package com.farmermarket.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farmermarket.dto.request.RegisterFarmerRequest;
import com.farmermarket.entities.Farmer;
import com.farmermarket.service.FarmerService;

@RestController
@RequestMapping("/api/farmers")
public class FarmerController {

    private final FarmerService farmerService;

    public FarmerController(FarmerService farmerService) {
        this.farmerService = farmerService;
    }

    @PostMapping
    public Farmer registerFarmer(@RequestBody RegisterFarmerRequest request) {
        return farmerService.registerFarmer(request);
    }

    // 🔒 ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public Farmer getFarmer(@PathVariable Long id) {
        return farmerService.getFarmerById(id);
    }

 // 🔒 ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Farmer> getAllFarmers() {
        return farmerService.getAllFarmers();
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/approve")
    public Farmer approveFarmer(@PathVariable Long id) {
        return farmerService.approveFarmer(id);
    }
    
    @PreAuthorize("hasRole('FARMER')")
    @GetMapping("/me")
    public Farmer getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        return farmerService.getFarmerByEmail(email);
    }


 
   
}
