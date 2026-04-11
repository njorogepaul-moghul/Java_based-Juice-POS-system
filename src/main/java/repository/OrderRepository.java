package repository;

import db.DBConnection;
import models.Order;
import models.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    // Save a completed order to DB
    public int saveOrder(Order order) {
        String sql = "INSERT INTO orders (total_amount, timestamp) VALUES (?, ?)";
        int generatedId = -1;
        try (PreparedStatement stmt = DBConnection.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, order.getTotalAmount());
            stmt.setTimestamp(2, Timestamp.valueOf(order.getTimestamp()));
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next())
                generatedId = keys.getInt(1);

        } catch (SQLException e) {
            System.out.println("Error saving order: " + e.getMessage());
        }
        return generatedId;
    }

    // Save each order item linked to the order
    public void saveOrderItems(int orderId, List<OrderItem> items) {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, subtotal) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
            for (OrderItem item : items) {
                stmt.setInt(1, orderId);
                stmt.setInt(2, item.getProduct().getId());
                stmt.setInt(3, item.getQuantity());
                stmt.setDouble(4, item.getSubtotal());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            System.out.println("Error saving order items: " + e.getMessage());
        }
    }

    // Fetch all orders
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Statement stmt = DBConnection.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("id"),
                        new ArrayList<>(),
                        rs.getDouble("total_amount"));
                orders.add(order);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching orders: " + e.getMessage());
        }
        return orders;
    }
}