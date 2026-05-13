import { Routes } from '@angular/router';
import { CheckoutComponent } from './components/checkout/checkout.component';

export const routes: Routes = [
  // When user goes to http://localhost:4200/checkout, this component will load
  { path: 'checkout', component: CheckoutComponent },
  
  // Default route: automatically redirect to checkout page
  { path: '', redirectTo: '/checkout', pathMatch: 'full' } 
];