package com.example.brokerr.service;

import com.example.brokerr.model.Stock;
import com.example.brokerr.repository.StockRepository;
import com.example.brokerr.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class StockService {

    private final StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public int updateStockData(){
        return stockRepository.updateStockData();
    }

    public List<Stock> getAllStocks() {
        return stockRepository.getAllStocks();
    }

    public BigDecimal getStockPrice(String stockName) {
        BigDecimal stockPrice = stockRepository.getStockPriceByName(stockName);
        if (stockPrice == null) {
            throw new IllegalArgumentException("Stock with the name " + stockName + " not found.");
        }
        return stockPrice;
    }

    public String getStockNameById(Long stockId) {
        String stockName = stockRepository.getStockNameById(stockId);
        if (stockName == null) {
            throw new IllegalArgumentException("Stock with the Id " + stockId + " not found.");
        }
        return stockName;
    }
}
