package models;

import java.util.List;
import java.time.LocalDateTime;

public class Order {
    private int orderId;
    private List<OrderItem> items;
    private double totalAmount;
    private LocalDateTime timestamp;

    public Order(int orderId, List<OrderItem> items, double totalAmount) {
        this.orderId = orderId;
        this.items = items;
        this.totalAmount = totalAmount;
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

    // Setter for total
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

}
