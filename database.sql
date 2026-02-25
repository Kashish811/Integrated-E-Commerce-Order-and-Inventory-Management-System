-- ============================================================
-- Integrated E-Commerce Order and Inventory Management System
-- Database Script
-- Database Name: supermart
-- ============================================================

-- Create Database
CREATE DATABASE IF NOT EXISTS supermart;
USE supermart;

-- ============================================================
-- CUSTOMER TABLE
-- ============================================================

CREATE TABLE Customer (
    CustomerID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(100) NOT NULL,
    LastName VARCHAR(100) NOT NULL,
    Email VARCHAR(150) UNIQUE NOT NULL,
    Password VARCHAR(150) NOT NULL,
    Contact VARCHAR(20),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- ADDRESS TABLE
-- ============================================================

CREATE TABLE Address (
    AddressID INT PRIMARY KEY AUTO_INCREMENT,
    CustomerID INT NOT NULL,
    HouseNo VARCHAR(50),
    Street VARCHAR(100),
    City VARCHAR(100),
    Province VARCHAR(100),
    Country VARCHAR(100),
    ZipCode VARCHAR(20),
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID)
        ON DELETE CASCADE
);

-- ============================================================
-- CATEGORY TABLE
-- ============================================================

CREATE TABLE Category (
    CategoryID INT PRIMARY KEY AUTO_INCREMENT,
    CategoryName VARCHAR(100) UNIQUE NOT NULL
);

-- ============================================================
-- PRODUCT TABLE
-- ============================================================

CREATE TABLE Product (
    ProductID INT PRIMARY KEY AUTO_INCREMENT,
    ProductName VARCHAR(150) NOT NULL,
    CategoryID INT,
    Price DECIMAL(10,2) NOT NULL,
    Description TEXT,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (CategoryID) REFERENCES Category(CategoryID)
        ON DELETE SET NULL
);

-- ============================================================
-- CART TABLE
-- ============================================================

CREATE TABLE Cart (
    CartID INT PRIMARY KEY AUTO_INCREMENT,
    CustomerID INT NOT NULL UNIQUE,
    DateCreated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID)
        ON DELETE CASCADE
);

-- ============================================================
-- CART PRODUCT TABLE
-- ============================================================

CREATE TABLE CartProduct (
    CartProductID INT PRIMARY KEY AUTO_INCREMENT,
    CartID INT NOT NULL,
    ProductID INT NOT NULL,
    Quantity INT NOT NULL CHECK (Quantity > 0),
    FOREIGN KEY (CartID) REFERENCES Cart(CartID)
        ON DELETE CASCADE,
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
        ON DELETE CASCADE
);

-- ============================================================
-- ORDERS TABLE
-- ============================================================

CREATE TABLE Orders (
    OrderID INT PRIMARY KEY AUTO_INCREMENT,
    CustomerID INT NOT NULL,
    AddressID INT NOT NULL,
    OrderDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    TotalAmount DECIMAL(10,2) NOT NULL,
    Status ENUM('PLACED','SHIPPED','DELIVERED','CANCELLED') DEFAULT 'PLACED',
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID),
    FOREIGN KEY (AddressID) REFERENCES Address(AddressID)
);

-- ============================================================
-- ORDERED PRODUCT TABLE
-- ============================================================

CREATE TABLE OrderedProduct (
    OrderedProductID INT PRIMARY KEY AUTO_INCREMENT,
    OrderID INT NOT NULL,
    ProductID INT NOT NULL,
    Quantity INT NOT NULL CHECK (Quantity > 0),
    Price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID)
        ON DELETE CASCADE,
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
);

-- ============================================================
-- REVIEW TABLE
-- ============================================================

CREATE TABLE Review (
    ReviewID INT PRIMARY KEY AUTO_INCREMENT,
    CustomerID INT NOT NULL,
    ProductID INT NOT NULL,
    Rating INT CHECK (Rating BETWEEN 1 AND 5),
    Comment TEXT,
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID),
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID),
    UNIQUE (CustomerID, ProductID)
);

-- ============================================================
-- INVENTORY TABLE
-- ============================================================

CREATE TABLE Inventory (
    InventoryID INT PRIMARY KEY AUTO_INCREMENT,
    ProductID INT UNIQUE NOT NULL,
    AvailableStock INT NOT NULL CHECK (AvailableStock >= 0),
    ReorderLevel INT DEFAULT 10,
    LastUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
        ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
        ON DELETE CASCADE
);

-- ============================================================
-- INVENTORY TRANSACTION TABLE
-- ============================================================

CREATE TABLE InventoryTransaction (
    TransactionID INT PRIMARY KEY AUTO_INCREMENT,
    ProductID INT NOT NULL,
    TransactionType ENUM('IN','OUT','ADJUSTMENT') NOT NULL,
    Quantity INT NOT NULL CHECK (Quantity > 0),
    TransactionDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ReferenceID INT NULL,
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
        ON DELETE CASCADE
);

-- ============================================================
-- SUPPLIER TABLE
-- ============================================================

CREATE TABLE Supplier (
    SupplierID INT PRIMARY KEY AUTO_INCREMENT,
    SupplierName VARCHAR(150) NOT NULL,
    ContactPerson VARCHAR(100),
    Phone VARCHAR(20),
    Email VARCHAR(150),
    Address TEXT
);

-- ============================================================
-- STOCK ADJUSTMENT TABLE
-- ============================================================

CREATE TABLE StockAdjustment (
    AdjustmentID INT PRIMARY KEY AUTO_INCREMENT,
    ProductID INT NOT NULL,
    OldStock INT NOT NULL,
    NewStock INT NOT NULL,
    Reason TEXT,
    AdjustedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
        ON DELETE CASCADE
);

-- ============================================================
-- END OF DATABASE SCRIPT
-- ============================================================
