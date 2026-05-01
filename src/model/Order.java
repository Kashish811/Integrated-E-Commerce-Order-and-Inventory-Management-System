package model;

public class Order {

    private int orderId;
    private double total;
    private String status;

        public Order(int orderId, double total, String status) {
        this.orderId = orderId;
        this.total = total;
        this.status = status;
    }

        // Method getOrderId
    public int getOrderId() { return orderId; }
    
        // Method getTotal
    public double getTotal() { return total; }
    
        // Method getStatus
    public String getStatus() { return status; }
}
