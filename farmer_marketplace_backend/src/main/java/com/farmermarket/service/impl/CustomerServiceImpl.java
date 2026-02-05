package com.farmermarket.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmermarket.dto.request.RegisterCustomerRequest;
import com.farmermarket.entities.Customer;
import com.farmermarket.exception.BadRequestException;
import com.farmermarket.exception.ResourceNotFoundException;
import com.farmermarket.repository.CustomerRepository;
import com.farmermarket.security.Role;
import com.farmermarket.security.User;
import com.farmermarket.security.UserRepository;
import com.farmermarket.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               UserRepository userRepository,
                               PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ================= REGISTER CUSTOMER =================
    

    @Override
    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found with id: " + customerId));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    @Override
    public Customer registerCustomer(RegisterCustomerRequest request) {

        // 🔴 FIX: prevent duplicate user
        if (userRepository.existsByUsername(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        User user = new User();
        user.setUsername(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CUSTOMER);
        user.setEnabled(true);

        userRepository.save(user);

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setUser(user);

        return customerRepository.save(customer);
    }

}
