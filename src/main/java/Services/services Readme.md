# Services Layer

## Overview
The `services` package implements the **business logic** of the Juice POS system.  
It acts as an intermediary between the **UI/main application** and the **data models**, keeping logic centralized and maintainable.

---

## Package Structure

services/
├── OrderService.java
└── ProductService.java


---

## Classes and Responsibilities

### 1. OrderService
- Create and manage orders.
- Add/remove items from an order.
- Calculate and update order totals.
- Ensures business logic is separate from models.

**Key Methods:**
- `createOrder(int orderId)` – returns a new `Order` with totals initialized to 0.
- `addItem(Order order, Product product, int quantity)` – adds an item to an order and updates totals.
- `updateTotal(Order order)` – recalculates the total price of the order.

**Example Usage:**
```java
OrderService orderService = new OrderService();
ProductService productService = new ProductService();

// Add a product
Product product = new Product(1, "Orange Juice", 120.0);
productService.addProduct(product);

// Create an order using the new 3-parameter constructor
Order order = orderService.createOrder(101);

// Add item to the order
orderService.addItem(order, product, 2);

System.out.println("Order total: " + order.getTotal()); // prints 240.0

---

### 2. ProductService
- Manage product catalog.
- Add, list, and fetch products.
- Centralizes product-related operations for easy maintenance.

**Key Methods:**
- `addProduct(Product product)` – adds a product to the list.
- `getAllProducts()` – returns all products.
- `getProductById(int productId)` – fetches a product by its ID.

---

## Usage
The `services` layer is called by the `main` application or UI layer.  
**Example:**
```java
OrderService orderService = new OrderService();
ProductService productService = new ProductService();

Product product = new Product(1, "Orange Juice", 120.0);
productService.addProduct(product);

Order order = orderService.createOrder(101);
orderService.addItem(order, product, 2);

System.out.println("Order total: " + order.getTotal());