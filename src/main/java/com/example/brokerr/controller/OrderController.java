package com.example.brokerr.controller;

import com.example.brokerr.model.Orders;
import com.example.brokerr.model.OrderRequest;
import com.example.brokerr.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Order oluşturma
    @PostMapping("/create")
    public ResponseEntity<Orders> createOrder(@RequestBody OrderRequest req) {

        try {
            Orders order = orderService.createOrder(req.getCustomerId(), req.getAssetName(), req.getSize(), req.getOrderSide());
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Order match etme
    @PostMapping("/match")
    public ResponseEntity<String> matchOrder(@RequestBody Orders order) {
        try {
            orderService.matchOrder(order.getId());
            return new ResponseEntity<>("Order matched successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Order match etme
    @PostMapping("/cancel")
    public ResponseEntity<String> cancelOrder(@RequestBody Orders order) {
        try {
            orderService.matchOrder(order.getId());
            return new ResponseEntity<>("Order canceled successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

