package com.farmermarket.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmermarket.dto.request.RegisterFarmerRequest;
import com.farmermarket.entities.Farmer;
import com.farmermarket.exception.BadRequestException;
import com.farmermarket.exception.ResourceNotFoundException;
import com.farmermarket.repository.FarmerRepository;
import com.farmermarket.security.Role;
import com.farmermarket.security.User;
import com.farmermarket.security.UserRepository;
import com.farmermarket.service.FarmerService;

@Service
@Transactional
public class FarmerServiceImpl implements FarmerService {

    private final FarmerRepository farmerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public FarmerServiceImpl(FarmerRepository farmerRepository,
                             UserRepository userRepository,
                             PasswordEncoder passwordEncoder) {
        this.farmerRepository = farmerRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Farmer registerFarmer(RegisterFarmerRequest request) {

        if (userRepository.existsByUsername(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        User user = new User();
        user.setUsername(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.FARMER);
        user.setEnabled(false);

        userRepository.save(user);

        Farmer farmer = new Farmer();
        farmer.setName(request.getName());
        farmer.setEmail(request.getEmail());
        farmer.setPhone(request.getPhone());
        farmer.setAddress(request.getAddress());
        farmer.setApproved(false);
        farmer.setUser(user);

        return farmerRepository.save(farmer);
    }

    @Override
    public Farmer getFarmerById(Long id) {
        return farmerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Farmer not found with id: " + id));
    }

    @Override
    public List<Farmer> getAllFarmers() {
        return farmerRepository.findAll();
    }

    @Override
    public Farmer approveFarmer(Long id) {

        Farmer farmer = getFarmerById(id);

        farmer.setApproved(true);

        User user = farmer.getUser();
        user.setEnabled(true);   // 🔥 THIS UNLOCKS LOGIN

        userRepository.save(user);

        return farmerRepository.save(farmer);
    }
    
    @Override
    public Farmer getFarmerByEmail(String email) {
        return farmerRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));
    }


}
