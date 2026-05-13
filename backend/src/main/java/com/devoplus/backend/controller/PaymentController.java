package com.devoplus.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devoplus.backend.dto.PaymentRequestDTO;
import com.devoplus.backend.dto.PaymentResponseDTO;
import com.devoplus.backend.service.PayHereService;

@RestController
@RequestMapping("/api/v1/payments")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {

    private final PayHereService payHereService;

    @Value("${payhere.merchant.id}")
    private String merchantId;

    public PaymentController(PayHereService payHereService) {
        this.payHereService = payHereService;
    }

    @PostMapping("/generate-hash")
    public ResponseEntity<PaymentResponseDTO> generateHash(@RequestBody PaymentRequestDTO requestDTO) {
        String hash = payHereService.generatePaymentHash(requestDTO.getOrderId(), requestDTO.getAmount());
        return ResponseEntity.ok(new PaymentResponseDTO(hash, merchantId));
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handlePayHereWebhook(
            @RequestParam String merchant_id,
            @RequestParam String order_id,
            @RequestParam String payment_id,
            @RequestParam String payhere_amount,
            @RequestParam String payhere_currency,
            @RequestParam String status_code,
            @RequestParam String md5sig) {

        // Verify the status code (2 means success)
        if ("2".equals(status_code)) {
            System.out.println("Payment Successful for Order: " + order_id);
            // Update your database here to mark the order as paid
        }

        // Must return 200 OK to PayHere
        return ResponseEntity.ok("OK");
    }
}
