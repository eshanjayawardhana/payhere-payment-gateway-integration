import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface PaymentRequest {
  orderId: string;
  amount: number;
}

export interface PaymentResponse {
  hash: string;
  merchantId: string;
}

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  constructor(private http: HttpClient) { }

  getPaymentHash(request: PaymentRequest): Observable<PaymentResponse> {
    return this.http.post<PaymentResponse>(`${environment.apiUrl}/generate-hash`, request);
  }
}