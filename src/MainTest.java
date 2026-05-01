import service.ProductService;
import service.CartService;
import service.OrderService;
import model.Product;

import java.util.List;

/**
 * Class MainTest
 * Simple comment for MainTest
 */
public class MainTest {

    public static void main(String[] args) {

        ProductService productService = new ProductService();
        CartService cartService = new CartService();
        OrderService orderService = new OrderService();
        List<Product> products = productService.getAllProducts();
        for (Product p : products) {
            System.out.println(p.getProductName() + " - Rs." + p.getPrice());
        }
        cartService.addToCart(1, 2);

        orderService.placeOrder(1, 1, cartService.getCartItems());
    }
}