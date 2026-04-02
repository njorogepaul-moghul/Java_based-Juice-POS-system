import models.Product;
import models.OrderItem;
import models.Order;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // We create products
        Product mango = new Product(1, "Mango Juice", 150.0, 20);
        Product apple = new Product(2, "Apple Juice", 120.0, 15);

        // we create order items
        OrderItem item1 = new OrderItem(mango, 2); // 2 mango
        OrderItem item2 = new OrderItem(apple, 1); // 1 apple

        // Adding items to list
        List<OrderItem> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        // Calculating total
        double total = item1.getSubtotal() + item2.getSubtotal();

        // Create order
        Order order = new Order(1, items, total);

        // Print results
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Time: " + order.getTimestamp());
        System.out.println("Items:");

        for (OrderItem item : order.getItems()) {
            System.out.println(item.getProduct().getName() +
                    " x" + item.getQuantity() +
                    " = " + item.getSubtotal());
        }

        System.out.println("Total: " + order.getTotalAmount());
    }
}