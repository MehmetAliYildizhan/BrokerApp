package com.example.brokerr.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Employee {
    @Id // ID otomatik olarak artan değer olacak
    private Long id;

    private String username;
    private String password;
    private String dateAdded;
    private String employmentType;
    private Boolean isAdmin;
    private int failedTryCount;

    public Employee() {}

    public Employee(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // Getter ve Setter metodları
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public int getFailedTryCount() {
        return failedTryCount;
    }

    public void setFailedTryCount(int failedTryCount) {
        this.failedTryCount = failedTryCount;
    }
}
