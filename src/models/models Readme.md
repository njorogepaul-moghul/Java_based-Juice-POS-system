# 🍹 Juice POS System (Java)

A simple Java-based Point of Sale (POS) system for managing juice sales, built as a foundational project with scalable architecture in mind.

---

## 🚀 Project Overview

This project simulates a basic POS workflow:
- Creating products (e.g., juices)
- Adding items to an order
- Calculating totals
- Recording timestamps for each sale

The system is structured using clean, modular design principles to support future expansion (UI, database, APIs).

---

## ⚙️ Tech Stack

- Java (JDK 21 - LTS)
- Visual Studio Code
- Java Extension Pack

---

## 📁 Project Structure


juice-pos/
└── src/
├── Main.java
└── models/
├── Product.java
├── OrderItem.java
└── Order.java


---

## 🧠 Core Components

### 1. Product
Represents a juice item with:
- ID
- Name
- Price
- Stock quantity

---

### 2. OrderItem
Represents a line item in an order:
- Product
- Quantity
- Subtotal (auto-calculated)

---

### 3. Order
Represents a full transaction:
- Order ID
- List of OrderItems
- Total amount
- Timestamp (auto-generated)

---

## 🔄 Current Functionality

- Create products
- Add multiple products to an order
- Automatically calculate subtotals and total price
- Capture timestamp of each order
- Display order details in console

---

## 🧪 Example Output


Order ID: 1
Time: 2026-03-31T13:45:22
Items:
Mango Juice x2 = 300.0
Apple Juice x1 = 120.0
Total: 420.0


---

## 🧩 Key Concepts Applied

- Object-Oriented Programming (OOP)
- Class modeling (Product, Order, OrderItem)
- Encapsulation (getters/setters)
- Constructor-based initialization
- Basic data structures (List)

---

## 🚧 Next Steps

- Implement Service Layer (business logic separation)
- Add inventory management (stock updates)
- Build a user interface (console or JavaFX)
- Integrate database (SQLite/MySQL)
- Add reporting (daily sales, analytics)