package com.devoplus.backend.dto;

public class PaymentResponseDTO {
    private String hash;
    private String merchantId;

    
    public PaymentResponseDTO(String hash, String merchantId) {
        this.hash = hash;
        this.merchantId = merchantId;
    }

    public String getHash() { return hash; }
    public String getMerchantId() { return merchantId; }
}