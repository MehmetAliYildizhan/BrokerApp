package com.example.brokerr.service;

import com.example.brokerr.model.Customer;
import com.example.brokerr.model.Employee;
import com.example.brokerr.repository.*;
import com.example.brokerr.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.brokerr.model.Asset;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final StockRepository stockRepository;
    private final CustomerRepository customerRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AssetRepository assetRepository;
    JWTUtil jwtUtil = new JWTUtil();

    @Autowired
    public TransactionService(TransactionRepository transactionRepository,StockRepository stockRepository,
                              CustomerRepository customerRepository, EnrollmentRepository enrollmentRepository,
                              AssetRepository assetRepository) {
        this.transactionRepository = transactionRepository;
        this.stockRepository = stockRepository;
        this.customerRepository = customerRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.assetRepository = assetRepository;
    }

    // Deposit
    public void deposit(Long customerId, BigDecimal amount, String empToken) {

        if(!jwtUtil.isTokenValid(empToken)){
            new RuntimeException("Invalid employee token");
        }
        Long employeeId = Long.valueOf(jwtUtil.extractSubject(empToken));

        Customer customer = customerRepository.findById(customerId);
        if(customer==null){
            new RuntimeException("Customer not found");
        }

        Employee employee = enrollmentRepository.getById(employeeId);
        if(employee==null){
            new RuntimeException("Employee not found");
        }

        if (employee.getId()!=customer.getEmployeeId() && employee.getAdmin()!=true) {
            throw new RuntimeException("Employee does not have permission to modify this customer's balance");
        }

        BigDecimal newAmount = customer.getBalance().add(amount);
        BigDecimal newUsableAmount = customer.getUsableBalance().add(amount);
        customerRepository.updateBalance(customerId,newAmount);
        customerRepository.updateUsableBalance(customerId,newUsableAmount);

    }

    // Withdraw
    public void withdraw(Long customerId, BigDecimal amount, String empToken) {

        if(!jwtUtil.isTokenValid(empToken)){
            new RuntimeException("Invalid employee token");
        }
        Long employeeId = Long.valueOf(jwtUtil.extractSubject(empToken));

        Customer customer = customerRepository.findById(customerId);
        if(customer==null){
            new RuntimeException("Customer not found");
        }

        Employee employee = enrollmentRepository.getById(employeeId);
        if(employee==null){
            new RuntimeException("Employee not found");
        }
        if (employee.getId()!=customer.getEmployeeId() && employee.getAdmin()!=true) {
            throw new RuntimeException("Employee does not have permission to modify this customer's balance");
        }

        if(!customer.getUsableBalance().equals(0) && customer.getUsableBalance().compareTo(amount)>0){
            BigDecimal newAmount = customer.getBalance().subtract(amount);
            BigDecimal newUsableAmount = customer.getUsableBalance().subtract(amount);
            customerRepository.updateBalance(customerId,newAmount);
            customerRepository.updateUsableBalance(customerId,newUsableAmount);
        }else{
            throw new RuntimeException("Yetersiz bakiye");
        }
    }

}
