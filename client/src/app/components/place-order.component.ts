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
  orderForm!: FormGroup;
  selectedItems: (MenuItem & { quantity: number })[] = [];
  totalAmount = 0;

  private readonly MIN_LENGTH = 3;
  private readonly ORDER_CONFIRMATION_KEY = 'orderConfirmation';

  constructor(
    private fb: FormBuilder,
    private restaurantSvc: RestaurantService,
    private router: Router
  ) {
    this.initializeForm();
  }

  ngOnInit(): void {
    this.loadSelectedItems();
    
    if (this.noItemsSelected()) {
      this.redirectToMenu();
    }
  }

  startOver(): void {
    this.restaurantSvc.clearOrder();
    this.redirectToMenu();
  }

  onSubmit(): void {
    if (!this.isFormValidWithItems()) {
      return;
    }

    const orderData = this.prepareOrderData();
    
    this.restaurantSvc.placeOrder(orderData)
      .then(this.handleOrderSuccess.bind(this))
      .catch(this.handleOrderError);
  }

  private initializeForm(): void {
    this.orderForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(this.MIN_LENGTH)]],
      password: ['', [Validators.required, Validators.minLength(this.MIN_LENGTH)]]
    });
  }

  private loadSelectedItems(): void {
    this.selectedItems = this.restaurantSvc.getSelectedItems();
    this.calculateTotal();
  }

  private calculateTotal(): void {
    this.totalAmount = this.restaurantSvc.getTotalPrice();
  }

  private noItemsSelected(): boolean {
    return this.selectedItems.length === 0;
  }

  private redirectToMenu(): void {
    this.router.navigate(['/']);
  }

  private isFormValidWithItems(): boolean {
    return this.orderForm.valid && !this.noItemsSelected();
  }

  private prepareOrderData(): any {
    const orderItems: OrderItem[] = this.selectedItems.map(item => ({
      menuItemId: item.id,
      quantity: item.quantity,
      price: item.price
    }));
    
    return {
      username: this.orderForm.value.username,
      password: this.orderForm.value.password,
      items: orderItems,
      totalPrice: this.totalAmount
    };
  }

  private handleOrderSuccess(response: any): void {
    if (response.status !== 'success') {
      throw new Error(response.message || 'Unknown error occurred');
    }
    
    const confirmationData = {
      orderId: response.orderId,
      paymentId: response.paymentId,
      date: response.date,
      total: response.total
    };
    
    sessionStorage.setItem(this.ORDER_CONFIRMATION_KEY, JSON.stringify(confirmationData));
    this.router.navigate(['/confirmation']);
  }

  private handleOrderError = (error: any): void => {
    console.error('Error placing order:', error);
    alert(error.message || 'Failed to place order. Please check your credentials and try again.');
  }
}