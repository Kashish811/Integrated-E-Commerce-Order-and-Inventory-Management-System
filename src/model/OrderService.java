package service;

import util.DatabaseConnection;
import model.CartItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class OrderService {

    public void placeOrder(int customerId, int addressId, List<CartItem> cartItems) {

        Connection con = null;

        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false);  // START TRANSACTION

            double totalAmount = 0;

            // Calculate total
            for (CartItem item : cartItems) {
                String priceQuery = "SELECT Price FROM Product WHERE ProductID=?";
                PreparedStatement ps = con.prepareStatement(priceQuery);
                ps.setInt(1, item.getProductId());
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    totalAmount += rs.getDouble("Price") * item.getQuantity();
                }
            }

            // Insert into Orders
            String orderQuery = "INSERT INTO Orders (CustomerID, AddressID, TotalAmount) VALUES (?, ?, ?)";
            PreparedStatement orderPs = con.prepareStatement(orderQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            orderPs.setInt(1, customerId);
            orderPs.setInt(2, addressId);
            orderPs.setDouble(3, totalAmount);
            orderPs.executeUpdate();

            ResultSet generatedKeys = orderPs.getGeneratedKeys();
            int orderId = 0;

            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
            }

            // Insert ordered products
            for (CartItem item : cartItems) {

                String productQuery = "SELECT Price FROM Product WHERE ProductID=?";
                PreparedStatement ps = con.prepareStatement(productQuery);
                ps.setInt(1, item.getProductId());
                ResultSet rs = ps.executeQuery();

                double price = 0;
                if (rs.next()) {
                    price = rs.getDouble("Price");
                }

                String insertItem = "INSERT INTO OrderedProduct (OrderID, ProductID, Quantity, Price) VALUES (?, ?, ?, ?)";
                PreparedStatement itemPs = con.prepareStatement(insertItem);
                itemPs.setInt(1, orderId);
                itemPs.setInt(2, item.getProductId());
                itemPs.setInt(3, item.getQuantity());
                itemPs.setDouble(4, price);
                itemPs.executeUpdate();
            }

            con.commit();  // COMMIT

            System.out.println("Order placed successfully!");

        } catch (Exception e) {
            try {
                if (con != null) {
                    con.rollback(); // ROLLBACK
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
