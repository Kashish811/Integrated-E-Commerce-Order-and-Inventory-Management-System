package model;

public class InventoryItem {
    private int productId;
    private String productName;
    private double price;
    private int categoryId;
    private int stock;

        public InventoryItem(int productId, String productName, double price, int categoryId, int stock) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.categoryId = categoryId;
        this.stock = stock;
    }

        // Method getProductId
    public int getProductId() { return productId; }
    
        // Method getProductName
    public String getProductName() { return productName; }
    
        // Method getPrice
    public double getPrice() { return price; }
    
        // Method getCategoryId
    public int getCategoryId() { return categoryId; }

        // Method getStock
    public int getStock() { return stock; }
}
