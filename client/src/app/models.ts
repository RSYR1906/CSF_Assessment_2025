// You may use this file to create any models
export interface MenuItem {
    id: string;
    name: string;
    price: number;
    description: string;
    quantity?: number;
}

export interface OrderItem {
    menuItemId: string;
    quantity: number;
}

export interface Order {
    items: OrderItem[];
    totalPrice: number;
    username?: string;
}

export interface OrderConfirmation {
    orderId: string;
    paymentId: string;
    date: string;
    total: number;
}