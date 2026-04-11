package Services;

import models.Order;
import models.OrderItem;
import models.Product;
import repository.OrderRepository;
import repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private OrderRepository orderRepo = new OrderRepository();
    private ProductRepository productRepo = new ProductRepository();

    public Order createOrder(int orderId) {
        return new Order(orderId, new ArrayList<>(), 0.0);
    }

    public void addItem(Order order, Product product, int quantity) {
        // check stock first
        if (product.getStock() < quantity) {
            System.out.println("Insufficient stock for: " + product.getName());
            return;
        }
        OrderItem item = new OrderItem(product, quantity);
        order.getItems().add(item);
        updateTotal(order);
    }

    public void updateTotal(Order order) {
        double total = 0.0;
        for (OrderItem item : order.getItems()) {
            total += item.getSubtotal();
        }
        order.setTotalAmount(total);
    }

    // Checkout — saves order + items, deducts stock
    public void checkout(Order order) {
        int generatedId = orderRepo.saveOrder(order);
        if (generatedId != -1) {
            orderRepo.saveOrderItems(generatedId, order.getItems());

            // deduct stock
            for (OrderItem item : order.getItems()) {
                Product p = item.getProduct();
                p.setStock(p.getStock() - item.getQuantity());
                productRepo.updateProduct(p);
            }
            System.out.println("Order saved with ID: " + generatedId);
        }
    }

    public List<Order> getAllOrders() {
        return orderRepo.getAllOrders();
    }
}