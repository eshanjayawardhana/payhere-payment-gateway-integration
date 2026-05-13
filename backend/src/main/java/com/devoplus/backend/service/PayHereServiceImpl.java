package com.devoplus.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

@Service
public class PayHereServiceImpl implements PayHereService {

   @Value("${payhere.merchant.id}")
    private String merchantId;

    @Value("${payhere.merchant.secret}")
    private String merchantSecret;

    @Value("${payhere.currency}")
    private String currency;

    public String generatePaymentHash(String orderId, double amount) {
        try {
            // Format amount to two decimal places exactly as PayHere requires
            String amountFormatted = String.format("%.2f", amount);

            // Hash the merchant secret first
            String hashedSecret = getMd5(merchantSecret).toUpperCase();

            // Construct the final string to be hashed
            String hashString = merchantId + orderId + amountFormatted + currency + hashedSecret;

            // Return the final hash
            return getMd5(hashString).toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate hash", e);
        }
    }

    private String getMd5(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }
    
}
