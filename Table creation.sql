-- Create Database
CREATE DATABASE OnlineShopping;
USE OnlineShopping;

-- 1️⃣ Customer Table
CREATE TABLE Customer (
    CustomerID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(100) NOT NULL,
    LastName VARCHAR(100) NOT NULL,
    Email VARCHAR(150) UNIQUE NOT NULL,
    Password VARCHAR(150) NOT NULL,
    Contact VARCHAR(20),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2️⃣ Address Table (Simplified)
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

-- 3️⃣ Category Table
CREATE TABLE Category (
    CategoryID INT PRIMARY KEY AUTO_INCREMENT,
    CategoryName VARCHAR(100) UNIQUE NOT NULL
);

-- 4️⃣ Product Table (Now includes Price & Stock)
CREATE TABLE Product (
    ProductID INT PRIMARY KEY AUTO_INCREMENT,
    ProductName VARCHAR(150) NOT NULL,
    CategoryID INT,
    Price DECIMAL(10,2) NOT NULL,
    Stock INT NOT NULL CHECK (Stock >= 0),
    Description TEXT,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (CategoryID) REFERENCES Category(CategoryID)
        ON DELETE SET NULL
);

-- 5️⃣ Cart Table (One cart per customer)
CREATE TABLE Cart (
    CartID INT PRIMARY KEY AUTO_INCREMENT,
    CustomerID INT UNIQUE,
    DateCreated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID)
        ON DELETE CASCADE
);

-- 6️⃣ CartProduct Table
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

-- 7️⃣ Orders Table
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

-- 8️⃣ OrderedProduct Table
CREATE TABLE OrderedProduct (
    OrderedProductID INT PRIMARY KEY AUTO_INCREMENT,
    OrderID INT NOT NULL,
    ProductID INT NOT NULL,
    Quantity INT NOT NULL,
    Price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID)
        ON DELETE CASCADE,
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
);

-- 9️⃣ Review Table
CREATE TABLE Review (
    ReviewID INT PRIMARY KEY AUTO_INCREMENT,
    CustomerID INT NOT NULL,
    ProductID INT NOT NULL,
    Rating INT CHECK (Rating BETWEEN 1 AND 5),
    Comment TEXT,
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID),
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
);