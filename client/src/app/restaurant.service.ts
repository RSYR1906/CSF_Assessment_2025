import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, firstValueFrom, throwError } from "rxjs";
import { MenuItem, OrderItem } from "./models";

@Injectable()
export class RestaurantService {

  private selectedMenuItems: Map<string, MenuItem & { quantity: number }> = new Map();

  constructor(private http: HttpClient) { }

  // Get menu items from backend
  getMenuItems(): Promise<any> {
    return firstValueFrom(this.http.get<any[]>("/api/menu"));
  }

  // Add an item to the order
  addItemToOrder(item: MenuItem): void {
    const existingItem = this.selectedMenuItems.get(item.id);
    
    if (existingItem) {
      existingItem.quantity += 1;
    } else {
      this.selectedMenuItems.set(item.id, { ...item, quantity: 1 });
    }
  }

  // Remove an item from the order
  removeItemFromOrder(itemId: string): void {
    const existingItem = this.selectedMenuItems.get(itemId);
    
    if (existingItem && existingItem.quantity > 1) {
      existingItem.quantity -= 1;
    } else {
      this.selectedMenuItems.delete(itemId);
    }
  }

  // Get the current quantity of an item
  getItemQuantity(itemId: string): number {
    return this.selectedMenuItems.get(itemId)?.quantity || 0;
  }

  // Get all selected items
  getSelectedItems(): (MenuItem & { quantity: number })[] {
    return Array.from(this.selectedMenuItems.values());
  }

  // Calculate the total number of items
  getTotalItemCount(): number {
    let count = 0;
    this.selectedMenuItems.forEach(item => {
      count += item.quantity;
    });
    return count;
  }

  // Calculate the total price
  getTotalPrice(): number {
    let total = 0;
    this.selectedMenuItems.forEach(item => {
      total += item.price * item.quantity;
    });
    return total;
  }

  // Clear all selected items
  clearOrder(): void {
    this.selectedMenuItems.clear();
  }

  // Submit the order to the backend
  placeOrder(orderData: {
    username: string;
    password: string;
    items: OrderItem[];
    totalPrice: number;
  }): Promise<any> {
    // Create a payload with all the order details
    const payload = {
      username: orderData.username,
      password: orderData.password,
      totalPrice: orderData.totalPrice,
      items: this.getSelectedItems().map(item => ({
        menuItemId: item.id,
        name: item.name,
        quantity: item.quantity,
        price: item.price
      }))
    };
    
    console.log('Sending order to backend:', payload);
    
    return firstValueFrom(
      this.http.post<any>("/api/food_order", payload, {
        headers: {
          'Content-Type': 'application/json'
        }
      }).pipe(
        catchError((error: HttpErrorResponse) => {
          console.error('HTTP Error:', error);
          if (error.status === 401) {
            return throwError(() => new Error('Invalid username or password'));
          }
          return throwError(() => new Error('Error placing order: ' + (error.error?.message || error.message || 'Unknown error')));
        })
      )
    );
  }
}