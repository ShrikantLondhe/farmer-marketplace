package com.farmermarket.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmermarket.entities.Farmer;
import com.farmermarket.entities.Product;
import com.farmermarket.entities.StockDetails;
import com.farmermarket.exception.BadRequestException;
import com.farmermarket.exception.ResourceNotFoundException;
import com.farmermarket.repository.FarmerRepository;
import com.farmermarket.repository.ProductRepository;
import com.farmermarket.repository.StockDetailsRepository;
import com.farmermarket.service.ProductService;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final StockDetailsRepository stockDetailsRepository;
    private final FarmerRepository farmerRepository;

    public ProductServiceImpl(
            ProductRepository productRepository,
            StockDetailsRepository stockDetailsRepository,
            FarmerRepository farmerRepository) {
        this.productRepository = productRepository;
        this.stockDetailsRepository = stockDetailsRepository;
        this.farmerRepository = farmerRepository;
    }

    // ✅ ADD PRODUCT (logged-in farmer)
    @Override
    public Product addProduct(Product product, String farmerEmail) {

        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new BadRequestException("Price must be greater than 0");
        }

        Farmer farmer = farmerRepository.findByEmail(farmerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));

        product.setFarmer(farmer);
        product.setActive(true);

        Product savedProduct = productRepository.save(product);

        // auto-create stock
        StockDetails stock = new StockDetails();
        stock.setProduct(savedProduct);
        stock.setQuantity(0);
        stockDetailsRepository.save(stock);

        return savedProduct;
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findByActiveTrue();
    }

    @Override
    public List<Product> getProductsByFarmer(String email) {

        Farmer farmer = farmerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));

        // ❗ IMPORTANT: NO active filter
        return productRepository.findByFarmer(farmer);
    }


    @Override
    public Product disableProduct(Long id) {
        Product product = getProductById(id);
        product.setActive(false);
        return productRepository.save(product);
    }

    @Override
    public Product enableProduct(Long id) {
        Product product = getProductById(id);
        product.setActive(true);
        return productRepository.save(product);
    }
    
   
    
}
