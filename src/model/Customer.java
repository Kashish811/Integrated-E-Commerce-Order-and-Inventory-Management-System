package model;

/**
 * Class Customer
 * Simple comment for Customer
 */
public class Customer {
    private int id;
    private String name;
    private String email;
    private String role;

        public Customer(int id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

        // Method getId
    public int getId() { return id; }
    
        // Method getName
    public String getName() { return name; }
    
        // Method getEmail
    public String getEmail() { return email; }
    
        // Method getRole
    public String getRole() { return role; }
}
