package vttp.batch5.csf.assessment.server.model;

public class OrderItem {

    private String menuItemId;
    private String name;
    private int quantity;
    private double price;
    private double subtotal;

    public OrderItem() {
    }

    public OrderItem(String menuItemId, int quantity, double price) {
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = price * quantity;
    }

    public OrderItem(String menuItemId, String name, int quantity, double price) {
        this.menuItemId = menuItemId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = price * quantity;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.subtotal = this.price * quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        this.subtotal = price * this.quantity;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "menuItemId='" + menuItemId + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", subtotal=" + subtotal +
                '}';
    }
}