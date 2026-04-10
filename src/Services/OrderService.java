package Services;

import models.Order;
import models.OrderItem;
import models.Product;

import java.util.ArrayList;

public class OrderService {

    // Create a new order
    public Order createOrder(int orderId) {
        return new Order(orderId, new ArrayList<>(), 0.0);
    }

    // Add item to an order
    public void addItem(Order order, Product product, int quantity) {
        OrderItem item = new OrderItem(product, quantity);
        order.getItems().add(item);
        updateTotal(order);
    }

    // Update total of an order
    public void updateTotal(Order order) {
        double total = 0.0;
        for (OrderItem item : order.getItems()) {
            total += item.getSubtotal();
        }
        order.setTotal(total);
    }

}