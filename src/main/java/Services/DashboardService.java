package Services;

import db.DBConnection;
import models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardService {

    // Total sales amount today
    public double getTodaySales() {
        String sql = "SELECT SUM(total_amount) FROM orders WHERE DATE(timestamp) = CURDATE()";
        try (Statement stmt = DBConnection.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next())
                return rs.getDouble(1);
        } catch (SQLException e) {
            System.out.println("Error fetching today sales: " + e.getMessage());
        }
        return 0.0;
    }

    // Total number of orders today
    public int getTodayOrderCount() {
        String sql = "SELECT COUNT(*) FROM orders WHERE DATE(timestamp) = CURDATE()";
        try (Statement stmt = DBConnection.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next())
                return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error fetching order count: " + e.getMessage());
        }
        return 0;
    }

    // Products with stock below threshold
    public List<Product> getLowStockProducts(int threshold) {
        List<Product> low = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE stock < ?";
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, threshold);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                low.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("category")));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching low stock: " + e.getMessage());
        }
        return low;
    }

    // Top selling product by quantity sold
    public String getTopSellingProduct() {
        String sql = """
                    SELECT p.name, SUM(oi.quantity) as total_qty
                    FROM order_items oi
                    JOIN products p ON oi.product_id = p.id
                    GROUP BY p.name
                    ORDER BY total_qty DESC
                    LIMIT 1
                """;
        try (Statement stmt = DBConnection.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next())
                return rs.getString("name") + " (" + rs.getInt("total_qty") + " sold)";
        } catch (SQLException e) {
            System.out.println("Error fetching top product: " + e.getMessage());
        }
        return "No data yet";
    }

    // Daily revenue for last 7 days (for chart)
    public List<double[]> getWeeklyRevenue() {
        List<double[]> data = new ArrayList<>();
        String sql = """
                    SELECT DATE(timestamp) as day, SUM(total_amount) as revenue
                    FROM orders
                    WHERE timestamp >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)
                    GROUP BY DATE(timestamp)
                    ORDER BY day ASC
                """;
        try (Statement stmt = DBConnection.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                data.add(new double[] {
                        rs.getDate("day").getTime(),
                        rs.getDouble("revenue")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error fetching weekly revenue: " + e.getMessage());
        }
        return data;
    }
}