package service;

import model.CartItem;
import java.util.ArrayList;
import java.util.List;

public class CartService {

    private List<CartItem> cart = new ArrayList<>();

    public void addToCart(int productId, int quantity) {
        cart.add(new CartItem(productId, quantity));
    }

    public List<CartItem> getCartItems() {
        return cart;
    }

    public void clearCart() {
        cart.clear();
    }
}