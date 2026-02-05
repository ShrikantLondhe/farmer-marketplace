package com.farmermarket.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmermarket.dto.response.StockResponseDTO;
import com.farmermarket.entities.StockDetails;
import com.farmermarket.mapper.StockMapper;
import com.farmermarket.service.StockDetailsService;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockDetailsService stockService;

    public StockController(StockDetailsService stockService) {
        this.stockService = stockService;
    }

    // ✅ View stock by product
    @GetMapping("/product/{productId}")
    public ResponseEntity<StockResponseDTO> getStockByProduct(
            @PathVariable Long productId) {

        StockDetails stock = stockService.getStockByProductId(productId);
        return ResponseEntity.ok(StockMapper.toDTO(stock));
    }

    // ✅ Increase stock (Farmer/Admin)
    @PreAuthorize("hasAnyRole('FARMER','ADMIN')")
    @PutMapping("/product/{productId}/add")
    public ResponseEntity<StockResponseDTO> addStock(
            @PathVariable Long productId,
            @RequestParam int quantity) {

        StockDetails stock = stockService.addStock(productId, quantity);
        return ResponseEntity.ok(StockMapper.toDTO(stock));
    }

    // ✅ Reduce stock (used during order)
    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/product/{productId}/reduce")
    public ResponseEntity<StockResponseDTO> reduceStock(
            @PathVariable Long productId,
            @RequestParam int quantity) {

        StockDetails stock = stockService.reduceStock(productId, quantity);
        return ResponseEntity.ok(StockMapper.toDTO(stock));
    }
}
