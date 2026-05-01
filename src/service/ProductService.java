package service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Product;
import util.CustomException;
import util.DatabaseConnection;

/**
 * Class ProductService
 * Simple comment for ProductService
 */
public class ProductService {
    // Method getAllProducts
    public List<Product> getAllProducts() throws Exception {
        List<Product> products = new ArrayList<>();

        String query = "SELECT ProductID, ProductName, Price FROM Product";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("ProductID"),
                        rs.getString("ProductName"),
                        rs.getDouble("Price")
                ));
            }
        }

        return products;
    }
    // Method addProduct
    public void addProduct(String name, double price, int categoryId) throws Exception {

        String productQuery = "INSERT INTO Product(ProductName, Price, CategoryID, Description) VALUES(?,?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection()) {

            conn.setAutoCommit(false);

            try (PreparedStatement productStmt = conn.prepareStatement(
                    productQuery,
                    Statement.RETURN_GENERATED_KEYS)) {
                productStmt.setString(1, name);
                productStmt.setDouble(2, price);
                productStmt.setInt(3, categoryId);
                productStmt.setString(4, "New product");

                productStmt.executeUpdate();

                ResultSet rs = productStmt.getGeneratedKeys();
                if (!rs.next()) {
                    throw new CustomException("Failed to create product");
                }

                int productId = rs.getInt(1);
                try (PreparedStatement inventoryStmt = conn.prepareStatement(
                        "INSERT INTO Inventory(ProductID, AvailableStock) VALUES(?, 0)"
                )) {
                    inventoryStmt.setInt(1, productId);
                    inventoryStmt.executeUpdate();
                }

                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
    // Method updateProduct
    public void updateProduct(int productId, String name, double price, int categoryId) throws Exception {

        String query = "UPDATE Product SET ProductName=?, Price=?, CategoryID=? WHERE ProductID=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, categoryId);
            ps.setInt(4, productId);

            int rows = ps.executeUpdate();

            if (rows == 0) {
                throw new CustomException("Product not found with ID: " + productId);
            }
        }
    }
    // Method deleteProduct
    public void deleteProduct(int productId) throws Exception {

        String query = "DELETE FROM Product WHERE ProductID=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, productId);

            int rows = ps.executeUpdate();

            if (rows == 0) {
                throw new CustomException("Product not found with ID: " + productId);
            }
        }
    }
    // Method getProductById
    public Product getProductById(int productId) throws Exception {

        String query = "SELECT ProductID, ProductName, Price FROM Product WHERE ProductID=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Product(
                        rs.getInt("ProductID"),
                        rs.getString("ProductName"),
                        rs.getDouble("Price")
                );
            } else {
                throw new CustomException("Product not found");
            }
        }
    }
}