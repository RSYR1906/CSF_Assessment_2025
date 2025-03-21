import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { OrderConfirmation } from '../models';

@Component({
  selector: 'app-confirmation',
  standalone: false,
  templateUrl: './confirmation.component.html',
  styleUrl: './confirmation.component.css'
})
export class ConfirmationComponent implements OnInit {
  orderDetails: OrderConfirmation = {
    orderId: '',
    paymentId: '',
    date: '',
    total: 0
  };

  constructor(private router: Router) { }

  ngOnInit(): void {
    // Retrieve the order confirmation details from session storage
    const confirmationData = sessionStorage.getItem('orderConfirmation');
    
    if (confirmationData) {
      try {
        this.orderDetails = JSON.parse(confirmationData);
      } catch (error) {
        console.error('Error parsing confirmation data:', error);
        // Handle error case
        this.returnToMenu();
      }
    } else {
      // No confirmation data found, redirect back to menu
      this.returnToMenu();
    }
  }

  returnToMenu(): void {
    // Clear session storage and return to menu
    sessionStorage.removeItem('orderConfirmation');
    this.router.navigate(['/']);
  }
}