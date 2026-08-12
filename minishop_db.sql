CREATE DATABASE IF NOT EXISTS minishop_db
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE minishop_db;

CREATE TABLE IF NOT EXISTS danh_muc (
    ma_dm INT AUTO_INCREMENT PRIMARY KEY,
    ten_dm VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS san_pham (
    ma_sp INT AUTO_INCREMENT PRIMARY KEY,
    ten_sp VARCHAR(100) NOT NULL,
    don_gia DECIMAL(12,2) NOT NULL,
    so_luong INT NOT NULL DEFAULT 0,
    ma_dm INT NULL,
    FOREIGN KEY (ma_dm) REFERENCES danh_muc(ma_dm)
);

CREATE TABLE IF NOT EXISTS khach_hang (
    ma_kh INT AUTO_INCREMENT PRIMARY KEY,
    ten_kh VARCHAR(100) NOT NULL,
    sdt VARCHAR(10) NOT NULL,
    dia_chi VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS hoa_don (
    ma_hd INT AUTO_INCREMENT PRIMARY KEY,
    ngay_lap DATE NOT NULL,
    ma_kh INT NOT NULL,
    tong_tien DECIMAL(12,2) DEFAULT 0,
    FOREIGN KEY (ma_kh) REFERENCES khach_hang(ma_kh)
);

CREATE TABLE IF NOT EXISTS chi_tiet_hoa_don (
    ma_hd INT NOT NULL,
    ma_sp INT NOT NULL,
    so_luong INT NOT NULL,
    don_gia DECIMAL(12,2) NOT NULL,
    thanh_tien DECIMAL(12,2) NOT NULL,
    PRIMARY KEY (ma_hd, ma_sp),
    FOREIGN KEY (ma_hd) REFERENCES hoa_don(ma_hd),
    FOREIGN KEY (ma_sp) REFERENCES san_pham(ma_sp)
);

CREATE TABLE IF NOT EXISTS tai_khoan (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    ho_ten VARCHAR(100) NOT NULL,
    vai_tro VARCHAR(20) NOT NULL
);

INSERT INTO danh_muc(ten_dm) VALUES
('Ban phim'), ('Chuot'), ('USB'), ('Tai nghe');

INSERT INTO san_pham(ten_sp, don_gia, so_luong, ma_dm)
SELECT 'Ban phim Logitech K120', 180000, 50, ma_dm
FROM danh_muc WHERE ten_dm='Ban phim';

INSERT INTO san_pham(ten_sp, don_gia, so_luong, ma_dm)
SELECT 'Chuot khong day Rapoo', 220000, 40, ma_dm
FROM danh_muc WHERE ten_dm='Chuot';

INSERT INTO san_pham(ten_sp, don_gia, so_luong, ma_dm)
SELECT 'USB Kingston 32GB', 150000, 100, ma_dm
FROM danh_muc WHERE ten_dm='USB';

INSERT INTO san_pham(ten_sp, don_gia, so_luong, ma_dm)
SELECT 'Tai nghe Sony Basic', 350000, 30, ma_dm
FROM danh_muc WHERE ten_dm='Tai nghe';

INSERT INTO khach_hang(ten_kh, sdt, dia_chi) VALUES
('Nguyen Van An', '0912345678', 'Ha Noi'),
('Tran Thi Binh', '0987654321', 'Bac Ninh'),
('Le Van Cuong', '0901111222', 'Hai Duong');

INSERT INTO tai_khoan(username, password, ho_ten, vai_tro) VALUES
('admin', '123', 'Quan tri vien', 'ADMIN'),
('nhanvien', '123', 'Nhan vien ban hang', 'NHANVIEN'),
('ketoan', '123', 'Ke toan', 'KETOAN');
