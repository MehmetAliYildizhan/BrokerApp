package com.example.brokerr.service;

import com.example.brokerr.model.Customer;
import com.example.brokerr.model.Asset;
import com.example.brokerr.repository.CustomerRepository;
import com.example.brokerr.repository.TransactionRepository;
import jakarta.persistence.TableGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    // Müşteri ekle
    @Transactional
    public void addCustomer(Customer customer) {

        Long maxId = customerRepository.findMaxId();
        Long newId = (maxId == -1) ? 0 : maxId + 1;
        customer.setId(newId);
        customer.setUsableBalance(customer.getBalance());
        customerRepository.addCustomer(customer);
    }

    // Tüm müşterileri al
    @Transactional
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // CustomerId ile varlıkları al
    @Transactional
    public List<Asset> getCustomerAssets(Long customerId) {
        return transactionRepository.getAssetsByCustomerId(customerId);
    }

    // CustomerId ile müşteri bilgilerini al
    @Transactional
    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId);
    }

    @Transactional
    public Customer getByUsername(String name){
        return customerRepository.findByName(name);
    }
    // Müşteriye varlık ekle (Deposit işlemi)
    @Transactional
    public void deposit(Long customerId, String stockName, Long quantity) {
        transactionRepository.deposit(customerId, stockName, quantity);
    }

    // Müşteriden varlık çek (Withdraw işlemi)
    @Transactional
    public void withdraw(Long customerId, String stockName, Long quantity) {
        transactionRepository.withdraw(customerId, stockName, quantity);
    }

    // Tüm varlıkları (Stock) al
    @Transactional
    public List<Asset> getAllStocks() {
        return transactionRepository.getAllStocks();
    }

    @Transactional
    public void updateBalance(Long customerId, BigDecimal balance) {
        customerRepository.updateBalance(customerId,balance);
    }

    @Transactional
    public void updateUsableBalance(Long customerId, BigDecimal usableBalance) {
        customerRepository.updateUsableBalance(customerId,usableBalance);
    }


}
