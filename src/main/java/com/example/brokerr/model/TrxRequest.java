package com.example.brokerr.model;

import java.math.BigDecimal;

public class TrxRequest {

    Long customerId;
    BigDecimal amount;
    String empToken;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getEmpToken() {
        return empToken;
    }

    public void setEmpToken(String empToken) {
        this.empToken = empToken;
    }
}
