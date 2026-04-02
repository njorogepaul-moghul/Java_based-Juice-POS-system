# Juice POS

## Overview
Juice POS is a **Point-of-Sale system** built in Java for managing products, orders, and sales.  
It follows a **modular architecture**, separating concerns between models, services, and the user interface.

---

## Project Structure

src/
├── models/ # Data models: Product, OrderItem, Order
├── services/ # Business logic: ProductService, OrderService
└── main/ # Entry point / CLI / future dashboard


---

## Layers

### 1. Models
- Hold application data.
- Classes: `Product`, `OrderItem`, `Order`.
- Contain only fields, getters, and setters.

### 2. Services
- Encapsulate business logic.
- Classes: `ProductService`, `OrderService`.
- Responsible for managing products, creating orders, and calculating totals.

### 3. Main / UI
- Entry point for the application.
- Interacts with services to perform tasks.
- Keeps the user interface separate from business logic.

---

## Example Usage
```java
ProductService productService = new ProductService();
OrderService orderService = new OrderService();

// Add product
Product product = new Product(1, "Orange Juice", 120.0);
productService.addProduct(product);

// Create order and add item
Order order = orderService.createOrder(101);
orderService.addItem(order, product, 2);

System.out.println("Order total: " + order.getTotal());