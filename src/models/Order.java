package models;

import java.util.List;
import java.time.LocalDateTime;

public class Order {
    private int orderId;
    private List<OrderItem> items;
    private double totalAmount;
    private LocalDateTime timestamp;
    private double total;

    public Order(int orderId, List<OrderItem> items, double total) {
        this.orderId = orderId;
        this.items = items;
        this.total = total;
        this.totalAmount = 0.0; // default
        this.timestamp = LocalDateTime.now();// auto timestamp
    }

    // getters
    public int getOrderId() {
        return orderId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getTotal() {
        return total;
    }

    // Setter for total
    public void setTotal(double total) {
        this.total = total;
    }

}
