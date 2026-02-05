package com.farmermarket.service;

import com.farmermarket.entities.StockDetails;

public interface StockDetailsService {

    StockDetails getStockByProductId(Long productId);

    StockDetails addStock(Long productId, int quantity);

    StockDetails reduceStock(Long productId, int quantity);
}
