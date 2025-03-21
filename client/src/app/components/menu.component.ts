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
    this.restaurantSvc.addItemToOrder(item);
    this.updateTotals();
  }

  removeItem(item: MenuItem): void {
    this.restaurantSvc.removeItemFromOrder(item.id);
    this.updateTotals();
  }

  getQuantity(itemId: string): number {
    return this.restaurantSvc.getItemQuantity(itemId);
  }

  updateTotals(): void {
    this.totalItems = this.restaurantSvc.getTotalItemCount();
    this.totalPrice = this.restaurantSvc.getTotalPrice();
  }

  placeOrder(): void {
    if (this.totalItems > 0) {
      console.log('Navigating to place-order with', this.totalItems, 'items');
      this.router.navigate(['/place-order']);
    }
  }
}