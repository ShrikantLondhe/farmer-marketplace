package com.farmermarket.service;

import java.util.List;

import com.farmermarket.dto.request.RegisterCustomerRequest;
import com.farmermarket.entities.Customer;

public interface CustomerService {

    Customer registerCustomer(RegisterCustomerRequest request);

    Customer getCustomerById(Long customerId);

    List<Customer> getAllCustomers();
}
