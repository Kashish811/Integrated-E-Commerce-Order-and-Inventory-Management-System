package model;
public class OrderItem {

    private String productName;
    private int quantity;
    private double price;

        public OrderItem(String productName, int quantity, double price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

        // Method getProductName
    public String getProductName() { return productName; }
    
        // Method getQuantity
    public int getQuantity() { return quantity; }
    
        // Method getPrice
    public double getPrice() { return price; }
}
