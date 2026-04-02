package models;

public class OrderService {
    public Order CreateOrder(int orderId) {
        return new order(orderId,new Array List<>(),0.0);
    }

    public void addItem(Order order, Product product, int quantity) {
        OrderItem Order = new orderItem(product, quantity);
        Order.getItems().add(Item);
        UpdateTotal(Order);
    }

    public void updateTotal(Orde order) {
        double total = 0;
        for (OrderItem Item : Order.getItems()) {
            total += item.getSubtotal();
        }
        order.setTotal(total);

    }

}
