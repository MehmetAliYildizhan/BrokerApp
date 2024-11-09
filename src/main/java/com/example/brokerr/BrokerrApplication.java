package com.example.brokerr;

import com.example.brokerr.repository.StockRepository;
import com.example.brokerr.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BrokerrApplication implements CommandLineRunner {

	private final StockService stockService;

	@Autowired
	public BrokerrApplication(StockService stockService) {
		this.stockService = stockService;
	}

	public static void main(String[] args) {
		SpringApplication.run(BrokerrApplication.class, args);
		System.out.println("BrokerApp Started.");
	}

	@Override
	public void run(String... args) throws Exception {

		stockService.updateStockData();
	}

}
