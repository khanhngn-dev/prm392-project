package com.example.project.model;

import java.util.List;
import java.util.UUID;

public class Order {
    private UUID id;
    private String userEmail;
    private List<Product> productList;
    private String phoneNumber;
    private String status;
    private double totalCost;

    public Order() {
    }

    public Order(UUID id, String userEmail, List<Product> productList, String phoneNumber, String status, double totalCost) {
        this.id = id;
        this.userEmail = userEmail;
        this.productList = productList;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.totalCost = totalCost;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}
