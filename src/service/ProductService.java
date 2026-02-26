package service;

import util.DatabaseConnection;
import model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

    public List<Product> getAllProducts() {

        List<Product> list = new ArrayList<>();

        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "SELECT ProductID, ProductName, Price FROM Product";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("ProductID"),
                        rs.getString("ProductName"),
                        rs.getDouble("Price")
                );
                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}