package model;

public class CartItem {

    private int productId;
    private String name;
    private int quantity;
    private double price;

        public CartItem(int productId, String name, int quantity, double price) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

        // Method getProductId
    public int getProductId() { return productId; }
    
        // Method getName
    public String getName() { return name; }
    
        // Method getQuantity
    public int getQuantity() { return quantity; }
    
        // Method getPrice
    public double getPrice() { return price; }
}
