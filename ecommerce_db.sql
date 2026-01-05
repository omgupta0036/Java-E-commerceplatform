-- 1. Setup Database
CREATE DATABASE IF NOT EXISTS ecommerce_db;
USE ecommerce_db;

-- 2. Products Table
CREATE TABLE IF NOT EXISTS products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL,
    stock INT NOT NULL
);

-- 3. Updated Orders Table (Matches Java DAO Logic)
-- We added product_id, quantity, and payment_method
CREATE TABLE IF NOT EXISTS orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    quantity INT DEFAULT 1,
    total_price DOUBLE NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. Initial Data
INSERT INTO products (name, price, stock) VALUES
('Laptop', 55000, 10),
('Smart Watch', 2500, 20),
('Earbuds', 1500, 25);

-- 5. Verification
DESC orders; -- Check if columns match Java PreparedStatement