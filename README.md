# Integrated-E-Commerce-Order-and-Inventory-Management-System
A Java-based Integrated E-Commerce Order and Inventory Management System implementing JDBC connectivity, transaction management, and JavaFX UI.

## 📌 Project Overview

The **Integrated E-Commerce Order and Inventory Management System** is a Java-based desktop application developed using **Java, JDBC, MySQL, and JavaFX**.

The system integrates complete e-commerce functionality with real-time inventory tracking, order processing, and transaction management. It demonstrates structured Object-Oriented Programming and professional database integration using JDBC.

This project was developed as part of the **Programming with Java** course.

---

## 🎯 Objectives

- Implement database connectivity using JDBC
- Perform complete CRUD operations
- Implement transaction management (Commit & Rollback)
- Integrate JavaFX UI with backend logic
- Apply Object-Oriented Programming principles
- Maintain inventory consistency using stock validation
- Track stock movement using inventory transactions

---

## 🛠️ Technologies Used

- Java (JDK 17+)
- JavaFX
- MySQL
- JDBC
- Git & GitHub
- VS Code

---

## 🗄️ Database Design

Database Name: `ecommerce`

The system uses a normalized relational database with proper primary keys, foreign keys, constraints, and referential integrity to ensure data consistency.

---

## 📋 Tables in the System (13 Tables)

| Table Name | Description |
|------------|------------|
| **Customer** | Stores registered customer details. |
| **Address** | Stores multiple addresses linked to customers. |
| **Category** | Stores product categories. |
| **Product** | Stores product details including price and description. |
| **Cart** | Maintains one active cart per customer. |
| **CartProduct** | Stores products added to cart before checkout. |
| **Orders** | Stores order details such as customer, total amount, status, and date. |
| **OrderedProduct** | Stores individual products included in each order. |
| **Review** | Stores customer ratings and comments for products. |
| **Inventory** | Maintains real-time stock for each product. |
| **InventoryTransaction** | Tracks stock movement (IN, OUT, ADJUSTMENT). |
| **Supplier** | Stores supplier details for inventory sourcing. |
| **StockAdjustment** | Logs manual inventory corrections performed by admin. |

---

## 🔗 Key Relationships

- One **Customer** → Many **Addresses**
- One **Customer** → One **Cart**
- One **Customer** → Many **Orders**
- One **Order** → Many **OrderedProduct**
- One **Product** → One **Inventory**
- One **Category** → Many **Products**
- One **Product** → Many **InventoryTransaction**
- One **Product** → Many **Reviews**
- One **Product** → Many **StockAdjustment**

---

## 🔐 Data Integrity Features

The database enforces:

- Primary keys on all tables
- Foreign key constraints
- UNIQUE constraint on Email
- UNIQUE(CustomerID, ProductID) for Reviews
- CHECK constraints (Stock ≥ 0, Quantity > 0, Rating between 1–5)
- ENUM type for Order Status and Inventory Transaction Type
- ON DELETE CASCADE rules for referential integrity

---

## ✨ Key Features

### 🔐 Authentication & Role Management
- Secure login validation
- Role-based access (Admin / Customer)

### 🛒 Customer Module
- Browse products
- Add to cart
- Modify cart
- Checkout
- View order history
- Submit product reviews

### 🛠️ Admin Module
- Add product
- Update product
- Delete product
- Monitor orders
- Manage inventory
- Adjust stock manually
- Track stock movement logs

### 📦 Inventory Management
- Real-time stock validation
- Automatic stock reduction after checkout
- Inventory transaction tracking
- Reorder level monitoring
- Manual stock adjustment logging

---

## 🔄 Transaction Management (Core Feature)

The checkout process uses professional transaction handling:

1. Disable auto-commit
2. Insert order record
3. Insert order items
4. Update inventory
5. Log inventory transaction
6. Commit on success
7. Rollback on failure

This ensures atomic operations and prevents data inconsistency.

---

## 🧠 OOP Concepts Implemented

- Encapsulation
- Abstraction
- Modular layered architecture
- Custom exception handling
- Separation of concerns

---

## 📂 Project Structure

````markdown
src/
│
├── model/
│   ├── Customer.java
│   ├── Address.java
│   ├── Category.java
│   ├── Product.java
│   ├── Cart.java
│   ├── CartItem.java
│   ├── Order.java
│   ├── OrderItem.java
│   ├── Review.java
│   ├── Inventory.java
│   ├── InventoryTransaction.java
│   ├── Supplier.java
│   └── StockAdjustment.java
│
├── service/
│   ├── CustomerService.java
│   ├── ProductService.java
│   ├── CartService.java
│   ├── OrderService.java
│   ├── InventoryService.java
│   ├── ReviewService.java
│   ├── SupplierService.java
│   └── PaymentService.java
│
├── controller/
│   ├── LoginController.java
│   ├── AdminDashboardController.java
│   ├── CustomerDashboardController.java
│   ├── CartController.java
│   ├── OrderHistoryController.java
│   └── ProductManagementController.java
│
├── util/
│   ├── DatabaseConnection.java
│   ├── AppConstants.java
│   └── CustomException.java
│
├── view/
│   ├── login.fxml
│   ├── admin_dashboard.fxml
│   ├── customer_dashboard.fxml
│   ├── cart.fxml
│   └── order_history.fxml
│
└── MainApp.java
````

---

## ▶️ How to Run

1. Clone the repository
2. Create MySQL database named `ecommerce`
3. Execute the SQL schema script
4. Configure database credentials in `AppConstants.java`
5. Run `MainApp.java`

---

## 🧪 Testing Scenarios

- Invalid login attempts
- Duplicate email prevention
- Insufficient stock handling
- Empty cart checkout prevention
- Transaction rollback verification
- Database connection failure handling
- Review uniqueness validation

---

## 👥 Contributors

- Kashish Chelwani
- Palak Goswami  
- Pruthvieraj Ghule
- Manikantan Menon

---

## 🚀 Future Enhancements

- Online payment gateway integration
- Email order confirmation
- Low-stock alert system
- Analytics dashboard
- Web deployment

---
