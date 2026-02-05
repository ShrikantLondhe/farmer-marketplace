package com.farmermarket.service;

import java.util.List;

import com.farmermarket.entities.Product;

public interface ProductService {

    Product addProduct(Product product, String farmerEmail);

    Product getProductById(Long productId);

    List<Product> getAllProducts();

    List<Product> getProductsByFarmer(String farmerEmail);

    Product disableProduct(Long productId);

    Product enableProduct(Long productId);
    
    
}