# 🍹 Juice POS System

A full-featured **Point of Sale (POS) system** built in Java for managing juice sales, orders, inventory, and reporting. Built with JavaFX for the UI and MySQL for persistent data storage.

---

## 📸 Preview

> Dashboard showing today's sales, low stock alerts, and revenue chart with green juice-themed UI.

---

## 🚀 Features

### 📊 Dashboard
- Today's total sales (Ksh)
- Today's order count
- Top selling product
- Low stock alerts table
- Daily revenue bar chart (last 7 days)

### 🛒 New Order
- Select products from a dropdown
- Add multiple items to a cart
- Real-time total calculation
- Stock validation before adding items
- Checkout with receipt popup
- Automatic stock deduction on checkout

### 📦 Products Management
- View all products in a table
- Add new products (ID, Name, Price, Stock, Category)
- Edit existing products
- Delete products
- All changes persist to MySQL database

### 🧾 Orders
- View all past orders from the database
- Order ID, total amount, and timestamp

---

## 🏗️ Architecture

The system follows a clean **Layered (N-Tier) Architecture**:

```
Presentation Layer  →  POSUI.java (JavaFX)
      ↓
Service Layer       →  OrderService, ProductService, DashboardService
      ↓
Repository Layer    →  OrderRepository, ProductRepository
      ↓
Data Layer          →  MySQL Database (via JDBC)
```

### Project Structure

```
Juice_pos/
├── pom.xml
├── .gitignore
├── README.md
└── src/
    └── main/
        ├── java/
        │   ├── POSUI.java               ← JavaFX entry point
        │   ├── models/
        │   │   ├── Product.java
        │   │   ├── Order.java
        │   │   └── OrderItem.java
        │   ├── Services/
        │   │   ├── ProductService.java
        │   │   ├── OrderService.java
        │   │   └── DashboardService.java
        │   ├── repository/
        │   │   ├── ProductRepository.java
        │   │   └── OrderRepository.java
        │   └── db/
        │       └── DBConnection.java    ← excluded from git
        └── resources/
            └── styles.css
```

---

## 🧰 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 (LTS) |
| UI Framework | JavaFX 21 |
| Database | MySQL 8.0 |
| DB Access | JDBC |
| Build Tool | Maven 3.9+ |
| IDE | Visual Studio Code |

---

## ⚙️ Prerequisites

Before running this project make sure you have:

- [JDK 21](https://adoptium.net/) installed
- [MySQL 8.0](https://dev.mysql.com/downloads/) installed and running
- [Maven 3.9+](https://maven.apache.org/download.cgi) installed and added to PATH
- [VS Code](https://code.visualstudio.com/) with Java Extension Pack (optional)

---

## 🗄️ Database Setup

**1. Open MySQL Workbench or MySQL CLI and run:**

```sql
CREATE DATABASE juice_pos;
USE juice_pos;

CREATE TABLE products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL,
    stock INT NOT NULL,
    category VARCHAR(50)
);

CREATE TABLE orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    total_amount DOUBLE NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE order_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    subtotal DOUBLE NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);
```

---

## 🔧 Installation & Setup

**1. Clone the repository:**
```bash
git clone https://github.com/njorogepaul-moghul/Java_based-Juice-POS-system.git
cd Java_based-Juice-POS-system
```

**2. Configure database credentials:**

Copy the template and fill in your credentials:
```bash
cp src/main/java/db/DBConnection.template.java src/main/java/db/DBConnection.java
```

Edit `DBConnection.java`:
```java
private static final String URL      = "jdbc:mysql://localhost:3306/juice_pos";
private static final String USER     = "your_mysql_username";
private static final String PASSWORD = "your_mysql_password";
```

**3. Run the application:**
```bash
mvn clean javafx:run
```

---

## 🖥️ Usage

### Adding Products
1. Click **Products** in the sidebar
2. Fill in the form fields (ID, Name, Price, Stock, Category)
3. Click **Add** — product is saved to the database

### Creating an Order
1. Click **New Order** in the sidebar
2. Select a product from the dropdown
3. Enter the quantity and click **Add to Order**
4. Repeat for more items
5. Click **Checkout** — receipt popup appears and stock is updated

### Viewing Reports
1. Click **Dashboard** in the sidebar
2. View today's sales, order count, top product
3. Check the revenue chart for the last 7 days
4. Low stock alerts show products with stock below 5

---

## 🗃️ Database Queries (Useful)

```sql
-- View all products
SELECT * FROM products;

-- View all orders
SELECT * FROM orders;

-- View order details with product names
SELECT o.id, p.name, oi.quantity, oi.subtotal, o.timestamp
FROM orders o
JOIN order_items oi ON o.id = oi.order_id
JOIN products p ON oi.product_id = p.id;

-- Today's total sales
SELECT SUM(total_amount) as today_sales 
FROM orders WHERE DATE(timestamp) = CURDATE();

-- Top selling products
SELECT p.name, SUM(oi.quantity) as total_sold
FROM order_items oi
JOIN products p ON oi.product_id = p.id
GROUP BY p.name
ORDER BY total_sold DESC;
```

---

## 🚧 Known Limitations

- No login/authentication system yet
- Receipt is displayed as a popup only (no printer support yet)
- Data is stored locally — no cloud/remote database support
- No export to PDF or Excel yet

---

## 🔮 Planned Features

- [ ] Login screen with role-based access (Admin / Cashier)
- [ ] Receipt printing support
- [ ] Export sales reports to PDF / Excel
- [ ] Product categories management
- [ ] Discount and tax configuration
- [ ] Customer management
- [ ] Cloud database support

---

## 👨‍💻 Author

**Paul Njoroge**  
GitHub: [@njorogepaul-moghul](https://github.com/njorogepaul-moghul)

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

