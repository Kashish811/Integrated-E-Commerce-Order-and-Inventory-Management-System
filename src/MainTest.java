import service.ProductService;
import service.CartService;
import service.OrderService;
import model.Product;

import java.util.List;

public class MainTest {

    public static void main(String[] args) {

        ProductService productService = new ProductService();
        CartService cartService = new CartService();
        OrderService orderService = new OrderService();

        // Show products
        List<Product> products = productService.getAllProducts();
        for (Product p : products) {
            System.out.println(p.getProductName() + " - Rs." + p.getPrice());
        }

        // Add to cart
        cartService.addToCart(1, 2); // productId 1, quantity 2

        // Place order
        orderService.placeOrder(1, 1, cartService.getCartItems());
    }
}