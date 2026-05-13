package com.devoplus.backend.dto;


public class PaymentRequestDTO {
    private String orderId;
    private double amount;

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}