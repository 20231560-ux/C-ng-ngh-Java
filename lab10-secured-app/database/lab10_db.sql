CREATE DATABASE IF NOT EXISTS lab10_db
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE lab10_db;

CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    active BOOLEAN DEFAULT TRUE
);

INSERT INTO users (email, password, full_name, role, active)
SELECT 'admin@gmail.com', '123456', 'Admin', 'ADMIN', TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email='admin@gmail.com');

INSERT INTO users (email, password, full_name, role, active)
SELECT 'staff@gmail.com', '123456', 'Staff', 'STAFF', TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email='staff@gmail.com');

INSERT INTO users (email, password, full_name, role, active)
SELECT 'user@gmail.com', '123456', 'User', 'USER', TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email='user@gmail.com');
