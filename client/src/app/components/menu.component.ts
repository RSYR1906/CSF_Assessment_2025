import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MenuItem } from '../models';
import { RestaurantService } from '../restaurant.service';

@Component({
  selector: 'app-menu',
  standalone: false,
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent implements OnInit {
  
  menuItems: MenuItem[] = [];
  totalPrice: number = 0;
  totalItems: number = 0;

  constructor(private restaurantSvc: RestaurantService, private router: Router) { }

  ngOnInit(): void {
    // Clear any previous selections when returning to the menu
    this.restaurantSvc.clearOrder();
    
    this.restaurantSvc.getMenuItems()
      .then(items => {
        console.log('Menu items received:', items);
        this.menuItems = items;
      })
      .catch(err => {
        console.error('Error fetching menu items:', err);
      });
  }

  addItem(item: MenuItem): void {
    // Add to the service
    this.restaurantSvc.addItemToOrder(item);
    
    // Update totals after adding
    this.updateTotals();
  }

  removeItem(item: MenuItem): void {
    // Remove from the service
    this.restaurantSvc.removeItemFromOrder(item.id);
    
    // Update totals after removing
    this.updateTotals();
  }

  getQuantity(itemId: string): number {
    // Get the quantity directly from the service
    return this.restaurantSvc.getItemQuantity(itemId);
  }

  updateTotals(): void {
    // Get totals directly from the service
    this.totalItems = this.restaurantSvc.getTotalItemCount();
    this.totalPrice = this.restaurantSvc.getTotalPrice();
  }

  placeOrder(): void {
    if (this.totalItems > 0) {
      console.log('Navigating to place-order with', this.totalItems, 'items');
      
      // The RestaurantService already has all the items with their quantities
      // so we can navigate directly
      this.router.navigate(['/place-order']);
    }
  }
}