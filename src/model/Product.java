package model;

public class Product {

    private int id;
    private String name;
    private double price;

        public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

        // Method getId
    public int getId() { return id; }
    
        // Method getName
    public String getName() { return name; }
    
        // Method getPrice
    public double getPrice() { return price; }
}
