import { Component, OnInit, PLATFORM_ID,Inject } from '@angular/core';
import { PaymentService, PaymentRequest } from '../../services/payment.service';
import { environment } from '../../../environments/environment';
import { isPlatformBrowser } from '@angular/common';

// Declare payhere to prevent TypeScript errors
declare var payhere: any;

@Component({
  selector: 'app-checkout',
  standalone: true,
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {

  constructor(private paymentService: PaymentService,@Inject(PLATFORM_ID) private platformId: Object) { }

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.setupPayHereCallbacks();
    }
  }

  private setupPayHereCallbacks(): void {
    // Called when payment is successfully completed
    payhere.onCompleted = function onCompleted(orderId: string) {
      console.log("Payment completed. OrderID:", orderId);
      // Navigate to success page or show message
    };

    // Called when user closes the payment popup
    payhere.onDismissed = function onDismissed() {
      console.log("Payment dismissed by user.");
    };

    // Called when an error occurs
    payhere.onError = function onError(error: string) {
      console.error("Payment error:", error);
    };
  }

  public initiatePayment(): void {

    if (!isPlatformBrowser(this.platformId) || typeof payhere === 'undefined') {
      console.error("PayHere script is not loaded or not in browser environment");
      return;
    }
    
    const orderId = "ORDER_" + new Date().getTime(); // Generate unique ID
    const amount = 1500.00;

    const request: PaymentRequest = { 
      orderId: orderId, 
      amount: amount 
    };

    // Step 1: Call Backend to get the Hash
    this.paymentService.getPaymentHash(request).subscribe({
      next: (response) => {
        
        // Step 2: Configure PayHere object
        const payment = {
          "sandbox": true,
          "merchant_id": response.merchantId,
          "return_url": `${environment.appUrl}/success`, 
          "cancel_url": `${environment.appUrl}/cancel`,
          "notify_url": "https://reaction-jolliness-curvature.ngrok-free.dev/api/v1/payments/webhook", // Must be public URL
          "order_id": orderId,
          "items": "Software Service",
          "amount": amount.toFixed(2),
          "currency": "LKR",
          "hash": response.hash,
          "first_name": "Eshan",
          "last_name": "Jayawardana",
          "email": "eshan@example.com",
          "phone": "0771234567",
          "address": "No 1, Main Street",
          "city": "Colombo",
          "country": "Sri Lanka"
        };

        // Step 3: Trigger the Payment Popup
        payhere.startPayment(payment);
      },
      error: (err) => {
        console.error("Failed to fetch payment hash from backend", err);
      }
    });
  }
}