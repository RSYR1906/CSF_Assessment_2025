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
  selectedItems: Map<string, number> = new Map<string, number>();
  totalPrice: number = 0;
  totalItems: number = 0;

  constructor(private restaurantSvc: RestaurantService, private router: Router) { }

  ngOnInit(): void {
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
    const currentQuantity = this.selectedItems.get(item.id) || 0;
    this.selectedItems.set(item.id, currentQuantity + 1);
    this.updateTotals();
  }

  removeItem(item: MenuItem): void {
    const currentQuantity = this.selectedItems.get(item.id) || 0;
    if (currentQuantity > 0) {
      this.selectedItems.set(item.id, currentQuantity - 1);
      this.updateTotals();
    }
  }

  getQuantity(itemId: string): number {
    return this.selectedItems.get(itemId) || 0;
  }

  updateTotals(): void {
    this.totalItems = 0;
    this.totalPrice = 0;
    
    this.selectedItems.forEach((quantity, itemId) => {
      if (quantity > 0) {
        const item = this.menuItems.find(i => i.id === itemId);
        if (item) {
          this.totalItems += quantity;
          this.totalPrice += item.price * quantity;
        }
      }
    });
  }

  placeOrder(): void {
    if (this.totalItems > 0) {
      // TODO: Navigate to place order page with selected items
      this.router.navigate(['/place-order']);
    }
  }
}