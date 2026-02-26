/*package util;

import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {
        try {
            Connection con = DatabaseConnection.getConnection();
            System.out.println("Connected Successfully!");
        } catch (Exception e) {
            System.out.println("Connection Failed!");
            e.printStackTrace();
        }
    }
}
*/
package util;

import dao.ProductDAO;
import model.Product;
import java.util.List;

public class TestConnection {

    public static void main(String[] args) {

        ProductDAO dao = new ProductDAO();
        List<Product> products = dao.getAllProducts();

        for (Product p : products) {
            System.out.println(p.getProductName() + " - Rs." + p.getPrice());
        }
    }
}