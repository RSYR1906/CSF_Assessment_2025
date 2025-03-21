import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MenuItem, OrderItem } from '../models';
import { RestaurantService } from '../restaurant.service';

@Component({
  selector: 'app-place-order',
  standalone: false,
  templateUrl: './place-order.component.html',
  styleUrl: './place-order.component.css'
})
export class PlaceOrderComponent implements OnInit {
  // TODO: Task 3
  orderForm: FormGroup;
  selectedItems: (MenuItem & { quantity: number })[] = [];
  totalAmount: number = 0;

  constructor(
    private fb: FormBuilder,
    private restaurantSvc: RestaurantService,
    private router: Router
  ) {
    // Initialize the form with validators
    this.orderForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  ngOnInit(): void {
    // Get the selected items from the service
    this.selectedItems = this.restaurantSvc.getSelectedItems();
    
    // Calculate the total amount
    this.calculateTotal();
    
    // If no items selected, redirect back to menu
    if (this.selectedItems.length === 0) {
      this.router.navigate(['/']);
    }
  }

  calculateTotal(): void {
    this.totalAmount = this.restaurantSvc.getTotalPrice();
  }

  startOver(): void {
    // Clear the order and navigate back to the menu
    this.restaurantSvc.clearOrder();
    this.router.navigate(['/']);
  }

  onSubmit(): void {
    if (this.orderForm.valid && this.selectedItems.length > 0) {
      // Prepare the order items
      const orderItems: OrderItem[] = this.selectedItems.map(item => ({
        menuItemId: item.id,
        quantity: item.quantity
      }));
      
      // Create the order data
      const orderData = {
        username: this.orderForm.value.username,
        password: this.orderForm.value.password,
        items: orderItems,
        totalPrice: this.totalAmount
      };
      
      // Submit the order
      this.restaurantSvc.placeOrder(orderData)
        .then(response => {
          if (response.status === 'success') {
            console.log('Order placed successfully', response);
            // Store order details for confirmation page
            sessionStorage.setItem('orderConfirmation', JSON.stringify({
              orderId: response.orderId,
              paymentId: response.paymentId,
              date: response.date,
              total: response.total
            }));
            // Navigate to confirmation page
            this.router.navigate(['/confirmation']);
          } else {
            // Handle error from server
            throw new Error(response.message || 'Unknown error occurred');
          }
        })
        .catch(error => {
          console.error('Error placing order', error);
          // Show error message to user
          alert(error.message || 'Failed to place order. Please check your credentials and try again.');
        });
    }
  }
}