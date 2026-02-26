package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/supermart";
        String user = "root";
        String password = "KC08";  // change this

        return DriverManager.getConnection(url, user, password);
    }
}