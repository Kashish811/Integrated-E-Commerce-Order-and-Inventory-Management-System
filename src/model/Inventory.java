package model;

/**
 * Class Inventory
 * Simple comment for Inventory
 */
public class Inventory {
    private int productId;
    private int stock;

        public Inventory(int productId, int stock) {
        this.productId = productId;
        this.stock = stock;
    }

        // Method getProductId
    public int getProductId() { return productId; }
    
        // Method getStock
    public int getStock() { return stock; }
}
