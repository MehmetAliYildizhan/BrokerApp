package com.example.brokerr.controller;

import com.example.brokerr.model.Transaction;
import com.example.brokerr.model.TrxRequest;
import com.example.brokerr.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Deposit işlemi
    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody TrxRequest request) {
        try {
            transactionService.deposit(request.getCustomerId(), request.getAmount(), request.getEmpToken());
            return ResponseEntity.ok("Deposit successful.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to deposit: " + e.getMessage());
        }
    }

    // Withdraw işlemi
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody TrxRequest request) {
        try {
            transactionService.withdraw(request.getCustomerId(), request.getAmount(), request.getEmpToken());
            return ResponseEntity.ok("Withdrawal successful.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to withdraw: " + e.getMessage());
        }
    }
}
