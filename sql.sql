-- ---------------------------------------------
-- 1️⃣ Create Database (Run only once)
-- ---------------------------------------------
CREATE DATABASE IF NOT EXISTS ecommerce_db;

-- Switch to database
USE ecommerce_db;

-- ---------------------------------------------
-- 2️⃣ Create Products Table
-- ---------------------------------------------
CREATE TABLE IF NOT EXISTS products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL,
    stock INT NOT NULL
);

-- Insert products only if not already added
INSERT INTO products (name, price, stock) VALUES
('Laptop', 55000, 10),
('Smart Watch', 2500, 20),
('Earbuds', 1500, 25);

-- ---------------------------------------------
-- 3️⃣ Create Orders Table (Matches Java Code)
-- ---------------------------------------------
CREATE TABLE IF NOT EXISTS orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(100) NOT NULL,
    amount DOUBLE NOT NULL,
    order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------
-- 4️⃣ Verify Tables (Optional)
-- ---------------------------------------------
SELECT * FROM products;
SELECT * FROM orders;
