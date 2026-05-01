package service;

import util.CustomException;
import util.DatabaseConnection;

import java.sql.*;

public class CustomerService {
    // Method login
    public int login(String email, String password) throws Exception {

        String query = "SELECT CustomerID FROM Customer WHERE Email=? AND Password=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("CustomerID");
            } else {
                throw new CustomException("Invalid email or password");
            }
        }
    }
    // Method getRole
    public String getRole(String email) {
        if (email.equalsIgnoreCase("admin@gmail.com")) {
            return "ADMIN";
        }

        return "CUSTOMER";
    }
}
