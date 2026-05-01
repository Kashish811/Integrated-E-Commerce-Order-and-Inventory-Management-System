package service;

import java.sql.*;
import util.DatabaseConnection;

public class InventoryService {
    // Method getStock
    public int getStock(int productId) throws Exception {
        Connection conn = DatabaseConnection.getConnection();

        PreparedStatement ps = conn.prepareStatement(
                "SELECT AvailableStock FROM Inventory WHERE ProductID=?"
        );
        ps.setInt(1, productId);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt("AvailableStock");

        return 0;
    }
    // Method updateStock
    public void updateStock(int productId, int newStock) throws Exception {
        Connection conn = DatabaseConnection.getConnection();

        PreparedStatement ps = conn.prepareStatement(
                "UPDATE Inventory SET AvailableStock=? WHERE ProductID=?"
        );
        ps.setInt(1, newStock);
        ps.setInt(2, productId);

        ps.executeUpdate();
    }
    // Method addStock
    public void addStock(int productId, int quantity) throws Exception {
        Connection conn = DatabaseConnection.getConnection();

        PreparedStatement ps = conn.prepareStatement(
                "UPDATE Inventory SET AvailableStock = AvailableStock + ? WHERE ProductID=?"
        );
        ps.setInt(1, quantity);
        ps.setInt(2, productId);

        int rows = ps.executeUpdate();

        if (rows == 0) {
            throw new Exception("Product not found in inventory");
        }
    }
    public java.util.List<model.InventoryItem> getAllInventory() throws Exception {
        java.util.List<model.InventoryItem> list = new java.util.ArrayList<>();
        String query = """
                SELECT p.ProductID, p.ProductName, p.Price, p.CategoryID, i.AvailableStock
                FROM Product p
                JOIN Inventory i ON p.ProductID = i.ProductID
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new model.InventoryItem(
                        rs.getInt("ProductID"),
                        rs.getString("ProductName"),
                        rs.getDouble("Price"),
                        rs.getInt("CategoryID"),
                        rs.getInt("AvailableStock")
                ));
            }
        }
        return list;
    }
}
