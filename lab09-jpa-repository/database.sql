-- Lab 9: Script tạo CSDL cho ứng dụng JPA
-- Lưu ý: hibernate.hbm2ddl.auto=update trong persistence.xml sẽ tự tạo/cập nhật bảng,
-- nhưng bạn vẫn cần tạo DATABASE trước khi chạy ứng dụng.

CREATE DATABASE IF NOT EXISTS lab09_jpa CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE lab09_jpa;

-- Các bảng dưới đây sẽ được Hibernate tự sinh ra khi chạy ứng dụng lần đầu (hbm2ddl.auto=update).
-- Được liệt kê ở đây để tham khảo cấu trúc.

CREATE TABLE IF NOT EXISTS lop_hoc (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ma_lop VARCHAR(20) NOT NULL UNIQUE,
    ten_lop VARCHAR(100) NOT NULL,
    khoa VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS sinh_vien (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ma_sinh_vien VARCHAR(20) NOT NULL UNIQUE,
    ho_ten VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    ngay_sinh DATE,
    lop_id INT,
    FOREIGN KEY (lop_id) REFERENCES lop_hoc(id)
);

CREATE TABLE IF NOT EXISTS mon_hoc (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ma_mon VARCHAR(20) NOT NULL UNIQUE,
    ten_mon VARCHAR(100) NOT NULL,
    so_tin_chi INT
);

CREATE TABLE IF NOT EXISTS diem (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sinh_vien_id INT NOT NULL,
    mon_hoc_id INT NOT NULL,
    diem_so DOUBLE,
    FOREIGN KEY (sinh_vien_id) REFERENCES sinh_vien(id),
    FOREIGN KEY (mon_hoc_id) REFERENCES mon_hoc(id)
);
