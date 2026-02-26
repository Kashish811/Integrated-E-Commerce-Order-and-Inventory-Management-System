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

CREATE TABLE InventoryTransaction (
    TransactionID INT PRIMARY KEY AUTO_INCREMENT,
    ProductID INT NOT NULL,
    TransactionType ENUM('IN','OUT','ADJUSTMENT') NOT NULL,
    Quantity INT NOT NULL CHECK (Quantity > 0),
    TransactionDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ReferenceID INT NULL, -- Can store OrderID if stock OUT
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
        ON DELETE CASCADE
);

CREATE TABLE Supplier (
    SupplierID INT PRIMARY KEY AUTO_INCREMENT,
    SupplierName VARCHAR(150) NOT NULL,
    ContactPerson VARCHAR(100),
    Phone VARCHAR(20),
    Email VARCHAR(150),
    Address TEXT
);

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
