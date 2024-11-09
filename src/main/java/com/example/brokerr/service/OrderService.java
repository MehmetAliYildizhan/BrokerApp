package com.example.brokerr.service;

import com.example.brokerr.model.Orders;
import com.example.brokerr.model.Customer;
import com.example.brokerr.model.Stock;
import com.example.brokerr.repository.CustomerRepository;
import com.example.brokerr.repository.OrderRepository;
import com.example.brokerr.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.brokerr.model.Asset;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
@Service
public class OrderService {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private StockService stockService;
    @Autowired
    private AssetService assetService;

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Orders createOrder(Long customerId, String stockName, BigDecimal size, String orderSide) {

        BigDecimal price = stockService.getStockPrice(stockName);
        BigDecimal orderValue = price.multiply(size);

        Customer customer = customerService.getCustomerById(customerId);

        if ("BUY".equals(orderSide)) {
            // For BUY orders, ensure the customer has enough usable balance
            if (customer.getUsableBalance().compareTo(orderValue) < 0) {
                throw new IllegalArgumentException("Insufficient usable balance.");
            }

            Long maxId = orderRepository.findMaxId();
            Long newId = (maxId == -1) ? 0 : maxId + 1;

            // Create the order
            Orders order = new Orders();
            order.setId(newId);
            order.setCustomerId(customerId);
            order.setAssetId(Long.valueOf(0));
            order.setOrderSide(orderSide);
            order.setSize(size);
            order.setPrice(price);
            order.setStatus("PENDING");
            order.setCreateDate(LocalDateTime.now());
            order.setOrderCode(stockName);

            // Persist the order in the repository
            orderRepository.createOrder(order);
            BigDecimal updatedUsableBalance = customer.getUsableBalance().subtract(orderValue);
            // Update customer's usable balance
            customerService.updateUsableBalance(customerId, updatedUsableBalance);

            return order;
        } else if ("SELL".equals(orderSide)) {
            // For SELL orders, ensure the customer has enough assets
            Asset customerAsset = assetService.getAssetByCustomerAndAssetName(customerId, stockName);
            if(customerAsset == null){
                new IllegalArgumentException("No asset found for customer.");
            }

            BigDecimal availableAsset = customerAsset.getUsableSize();
            if (availableAsset.compareTo(size) < 0) {
                throw new IllegalArgumentException("Insufficient assets for sell order.");
            }

            Long maxId = orderRepository.findMaxId();
            Long newId = (maxId == -1) ? 0 : maxId + 1;

            Orders order = new Orders();
            order.setId(newId);
            order.setCustomerId(customerId);
            order.setAssetId(Long.valueOf(0));
            order.setOrderSide(orderSide);
            order.setSize(size);
            order.setPrice(price);
            order.setStatus("PENDING");
            order.setCreateDate(LocalDateTime.now());
            order.setOrderCode(stockName);

            orderRepository.createOrder(order);

            return order;
        } else {
            throw new IllegalArgumentException("Invalid order side. Must be 'BUY' or 'SELL'.");
        }
    }


    public void matchOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId);
        if ("PENDING".equals(order.getStatus())) {
            orderRepository.updateOrderStatus(orderId, "CANCELED");
        }else{
            throw new IllegalArgumentException("This order cant be cancel");
        }
    }

    public void cancelOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId);
        if ("PENDING".equals(order.getStatus())) {
            BigDecimal refundableAmount = order.getPrice().multiply(order.getSize());
            if ("BUY".equals(order.getOrderSide())) {
                customerService.updateUsableBalance(order.getCustomerId(),
                        customerService.getCustomerById(order.getCustomerId()).getUsableBalance().add(refundableAmount));
            } else if ("SELL".equals(order.getOrderSide())) {
                // Refund the asset if the order is canceled, revert the asset size
                Asset customerAsset = assetService.getAssetByCustomerAndAssetName(order.getCustomerId(),
                                stockService.getStockNameById(order.getAssetId()));
                if(customerAsset==null){
                    new IllegalArgumentException("No asset found for customer.");
                }
            }
            orderRepository.cancelOrder(orderId);
        }
    }
}
