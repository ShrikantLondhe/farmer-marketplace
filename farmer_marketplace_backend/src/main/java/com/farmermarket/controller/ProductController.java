package com.farmermarket.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farmermarket.dto.response.ProductResponseDTO;
import com.farmermarket.entities.Product;
import com.farmermarket.mapper.ProductMapper;
import com.farmermarket.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // ✅ Farmer adds product
    @PreAuthorize("hasRole('FARMER')")
    @PostMapping
    public ResponseEntity<ProductResponseDTO> addProduct(
            @RequestBody Product product,
            Authentication authentication) {

        String email = authentication.getName();
        Product savedProduct = productService.addProduct(product, email);
        return ResponseEntity.ok(ProductMapper.toDTO(savedProduct));
    }


    // ✅ Customer views all products
    @PreAuthorize("hasAnyRole('CUSTOMER','FARMER','ADMIN')")
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {

        return ResponseEntity.ok(
                productService.getAllProducts()
                        .stream()
                        .map(ProductMapper::toDTO)
                        .toList()
        );
    }

    // ✅ View single product
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {

        Product product = productService.getProductById(id);
        return ResponseEntity.ok(ProductMapper.toDTO(product));
    }

    // ✅ Farmer views own products
    @PreAuthorize("hasRole('FARMER')")
    @GetMapping("/farmer")
    public ResponseEntity<List<ProductResponseDTO>> getMyProducts(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                productService.getProductsByFarmer(email)
                        .stream()
                        .map(ProductMapper::toDTO)
                        .toList()
        );
    }
    
    @PreAuthorize("hasRole('FARMER')")
    @PutMapping("/{id}/disable")
    public ResponseEntity<Void> disableProduct(@PathVariable Long id) {
        productService.disableProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('FARMER')")
    @PutMapping("/{id}/enable")
    public ResponseEntity<Void> enableProduct(@PathVariable Long id) {
        productService.enableProduct(id);
        return ResponseEntity.noContent().build();
    }



}
