package com.devoplus.backend.service;

public interface PayHereService {

    String generatePaymentHash(String orderId, double amount);
    
}
