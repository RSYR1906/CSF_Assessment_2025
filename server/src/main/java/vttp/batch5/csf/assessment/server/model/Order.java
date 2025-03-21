package vttp.batch5.csf.assessment.server.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    private String order_id;
    private String payment_id;
    private String username;
    private double total;
    private LocalDateTime timestamp;
    private List<OrderItem> items;

    public Order() {
    }

    public Order(String orderId, String paymentId, String username, double total, List<OrderItem> items) {
        this.order_id = orderId;
        this.payment_id = paymentId;
        this.username = username;
        this.total = total;
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", order_id='" + order_id + '\'' +
                ", payment_id='" + payment_id + '\'' +
                ", username='" + username + '\'' +
                ", total=" + total +
                ", timestamp=" + timestamp +
                ", items=" + items +
                '}';
    }
}