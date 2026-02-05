package com.farmermarket.service.impl;

import org.springframework.stereotype.Service;

import com.farmermarket.entities.StockDetails;
import com.farmermarket.repository.ProductRepository;
import com.farmermarket.repository.StockDetailsRepository;
import com.farmermarket.service.StockDetailsService;

@Service
public class StockDetailsServiceImpl implements StockDetailsService {

    private final StockDetailsRepository stockRepo;
    private final ProductRepository productRepo;

    public StockDetailsServiceImpl(StockDetailsRepository stockRepo,
                                   ProductRepository productRepo) {
        this.stockRepo = stockRepo;
        this.productRepo = productRepo;
    }

    @Override
    public StockDetails getStockByProductId(Long productId) {
        return stockRepo.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
    }

    @Override
    public StockDetails addStock(Long productId, int quantity) {
        if (quantity <= 0)
            throw new RuntimeException("Quantity must be positive");

        StockDetails stock = getStockByProductId(productId);
        stock.setQuantity(stock.getQuantity() + quantity);
        return stockRepo.save(stock);
    }

    @Override
    public StockDetails reduceStock(Long productId, int quantity) {
        StockDetails stock = getStockByProductId(productId);

        if (stock.getQuantity() < quantity)
            throw new RuntimeException("Insufficient stock");

        stock.setQuantity(stock.getQuantity() - quantity);
        return stockRepo.save(stock);
    }
}
