package com.example.brokerr.model;

import org.springframework.web.bind.annotation.RequestHeader;

public class LoginResponse {
    ReturnHeader header;
    String empToken;

    public ReturnHeader getHeader() {
        return header;
    }

    public void setHeader(ReturnHeader header) {
        this.header = header;
    }

    public String getEmpToken() {
        return empToken;
    }

    public void setEmpToken(String empToken) {
        this.empToken = empToken;
    }
}
