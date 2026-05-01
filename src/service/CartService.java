package service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.CartItem;
import util.CustomException;
import util.DatabaseConnection;

public class CartService {
    // Method addToCart
    public void addToCart(int customerId, int productId, int quantity) throws Exception {

        if (quantity <= 0) {
            throw new CustomException("Quantity must be greater than 0");
        }

        try (Connection conn = DatabaseConnection.getConnection()) {

            int cartId = getOrCreateCart(conn, customerId);
            String checkProduct = "SELECT ProductID FROM Product WHERE ProductID=?";
            try (PreparedStatement ps = conn.prepareStatement(checkProduct)) {
                ps.setInt(1, productId);
                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    throw new CustomException("Product does not exist");
                }
            }
            String checkQuery = "SELECT Quantity FROM CartProduct WHERE CartID=? AND ProductID=?";

            try (PreparedStatement check = conn.prepareStatement(checkQuery)) {
                check.setInt(1, cartId);
                check.setInt(2, productId);

                ResultSet rs = check.executeQuery();

                if (rs.next()) {
                    String updateQuery = "UPDATE CartProduct SET Quantity = Quantity + ? WHERE CartID=? AND ProductID=?";
                    try (PreparedStatement ps = conn.prepareStatement(updateQuery)) {
                        ps.setInt(1, quantity);
                        ps.setInt(2, cartId);
                        ps.setInt(3, productId);
                        ps.executeUpdate();
                    }
                } else {
                    String insertQuery = "INSERT INTO CartProduct(CartID, ProductID, Quantity) VALUES(?,?,?)";
                    try (PreparedStatement ps = conn.prepareStatement(insertQuery)) {
                        ps.setInt(1, cartId);
                        ps.setInt(2, productId);
                        ps.setInt(3, quantity);
                        ps.executeUpdate();
                }
            }
        }
    }
}
    // Method updateCartQuantity
    public void updateCartQuantity(int customerId, int productId, int newQuantity) throws Exception {
        if (newQuantity <= 0) {
            removeFromCart(customerId, productId);
            return;
        }

        String query = """
                UPDATE CartProduct cp
                JOIN Cart c ON cp.CartID = c.CartID
                SET cp.Quantity=?
                WHERE c.CustomerID=? AND cp.ProductID=?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, newQuantity);
            ps.setInt(2, customerId);
            ps.setInt(3, productId);

            if (ps.executeUpdate() == 0) {
                throw new CustomException("Item not found in cart");
            }
        }
    }
    // Method removeFromCart
    public void removeFromCart(int customerId, int productId) throws Exception {

        String query = """
                DELETE cp FROM CartProduct cp
                JOIN Cart c ON cp.CartID = c.CartID
                WHERE c.CustomerID=? AND cp.ProductID=?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, customerId);
            ps.setInt(2, productId);

            if (ps.executeUpdate() == 0) {
                throw new CustomException("Item not found in cart");
            }
        }
    }
    // Method getCartItems
    public List<CartItem> getCartItems(int customerId) throws Exception {

        List<CartItem> list = new ArrayList<>();

        String query = """
                SELECT p.ProductID, p.ProductName, cp.Quantity, p.Price
                FROM CartProduct cp
                JOIN Cart c ON cp.CartID = c.CartID
                JOIN Product p ON p.ProductID = cp.ProductID
                WHERE c.CustomerID = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new CartItem(
                        rs.getInt("ProductID"),
                        rs.getString("ProductName"),
                        rs.getInt("Quantity"),
                        rs.getDouble("Price")
                ));
            }
        }

        return list;
    }
    // Method clearCart
    public void clearCart(int customerId) throws Exception {

        String query = """
                DELETE cp FROM CartProduct cp
                JOIN Cart c ON cp.CartID = c.CartID
                WHERE c.CustomerID=?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, customerId);

            int rows = ps.executeUpdate();

            if (rows == 0) {
                throw new CustomException("Cart is already empty");
            }
        }
    }
    // Method getCartTotal
    public double getCartTotal(int customerId) throws Exception {

        double total = 0;

        for (CartItem item : getCartItems(customerId)) {
            total += item.getPrice() * item.getQuantity();
        }

        return total;
    }
    private int getOrCreateCart(Connection conn, int customerId) throws Exception {

        String findQuery = "SELECT CartID FROM Cart WHERE CustomerID=?";

        try (PreparedStatement ps = conn.prepareStatement(findQuery)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("CartID");
            }
        }
        String createQuery = "INSERT INTO Cart(CustomerID) VALUES(?)";

        try (PreparedStatement ps = conn.prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, customerId);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        throw new CustomException("Failed to create cart");
    }
}
