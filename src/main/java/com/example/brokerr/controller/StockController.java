package com.example.brokerr.controller;

import com.example.brokerr.model.Employee;
import com.example.brokerr.model.Stock;
import com.example.brokerr.service.StockService;
import com.example.brokerr.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockController {
    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }
    @PostMapping("/updateStockData")
    public void updateStockData(){

        int result = stockService.updateStockData();
        return;
    }

    @GetMapping("/list")
    public List<Stock> listAllStocks() {
        return stockService.getAllStocks();
    }

    @GetMapping("/getStockPrice/{stockName}")
    public ResponseEntity<String> getStockPrice(@PathVariable String stockName) {
        try {
            BigDecimal stockPrice = stockService.getStockPrice(stockName);
            return ResponseEntity.ok("The price of " + stockName + " is " + stockPrice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  // Stock not found
        }
    }

}
