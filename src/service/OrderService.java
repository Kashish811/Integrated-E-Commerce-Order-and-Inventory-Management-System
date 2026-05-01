package service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Order;
import model.OrderItem;
import util.CustomException;
import util.DatabaseConnection;

/**
 * Class OrderService
 * Simple comment for OrderService
 */
public class OrderService {
    // Method checkout
    public void checkout(int customerId) throws Exception {

        try (Connection conn = DatabaseConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {
                int addressId = 1;

                double total = 0;
                boolean hasItems = false;
                PreparedStatement orderStmt = conn.prepareStatement(
                        "INSERT INTO Orders(CustomerID, AddressID, TotalAmount, Status) VALUES(?,?,0,'PLACED')",
                        Statement.RETURN_GENERATED_KEYS
                );

                orderStmt.setInt(1, customerId);
                orderStmt.setInt(2, addressId);
                orderStmt.executeUpdate();

                ResultSet rs = orderStmt.getGeneratedKeys();
                if (!rs.next()) throw new CustomException("Order creation failed");

                int orderId = rs.getInt(1);
                PreparedStatement cartStmt = conn.prepareStatement("""
                    SELECT cp.ProductID, cp.Quantity, p.Price
                    FROM CartProduct cp
                    JOIN Cart c ON cp.CartID = c.CartID
                    JOIN Product p ON p.ProductID = cp.ProductID
                    WHERE c.CustomerID = ?
                """);

                cartStmt.setInt(1, customerId);
                ResultSet cartItems = cartStmt.executeQuery();

                while (cartItems.next()) {
                    hasItems = true;

                    int pid = cartItems.getInt("ProductID");
                    int qty = cartItems.getInt("Quantity");
                    double price = cartItems.getDouble("Price");
                    PreparedStatement op = conn.prepareStatement(
                            "INSERT INTO OrderedProduct(OrderID, ProductID, Quantity, Price) VALUES(?,?,?,?)"
                    );

                    op.setInt(1, orderId);
                    op.setInt(2, pid);
                    op.setInt(3, qty);
                    op.setDouble(4, price);
                    op.executeUpdate();
                    PreparedStatement inv = conn.prepareStatement(
                            "UPDATE Inventory SET AvailableStock = AvailableStock - ? WHERE ProductID=? AND AvailableStock >= ?"
                    );

                    inv.setInt(1, qty);
                    inv.setInt(2, pid);
                    inv.setInt(3, qty);

                    if (inv.executeUpdate() == 0) {
                        throw new CustomException("Insufficient stock for Product ID: " + pid);
                    }

                    total += price * qty;
                }

                if (!hasItems) {
                    throw new CustomException("Cart is empty");
                }
                PreparedStatement totalStmt = conn.prepareStatement(
                        "UPDATE Orders SET TotalAmount=? WHERE OrderID=?"
                );

                totalStmt.setDouble(1, total);
                totalStmt.setInt(2, orderId);
                totalStmt.executeUpdate();
                PreparedStatement clear = conn.prepareStatement("""
                    DELETE cp FROM CartProduct cp
                    JOIN Cart c ON cp.CartID = c.CartID
                    WHERE c.CustomerID = ?
                """);

                clear.setInt(1, customerId);
                clear.executeUpdate();

                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
    // Method getOrders
    public List<Order> getOrders(int customerId) throws Exception {

        List<Order> list = new ArrayList<>();

        String query = "SELECT OrderID, TotalAmount, Status FROM Orders WHERE CustomerID=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Order(
                        rs.getInt("OrderID"),
                        rs.getDouble("TotalAmount"),
                        rs.getString("Status")
                ));
            }
        }

        return list;
    }
    // Method getOrderItems
    public List<OrderItem> getOrderItems(int orderId) throws Exception {

        List<OrderItem> list = new ArrayList<>();

        String query = """
            SELECT p.ProductName, op.Quantity, op.Price
            FROM OrderedProduct op
            JOIN Product p ON p.ProductID = op.ProductID
            WHERE op.OrderID = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new OrderItem(
                        rs.getString("ProductName"),
                        rs.getInt("Quantity"),
                        rs.getDouble("Price")
                ));
            }
        }

        return list;
    }
}