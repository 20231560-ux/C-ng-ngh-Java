# LAB 5 - MiniShop Java Swing + JDBC (Eclipse)

## 1. Yêu cầu
- JDK 17 hoặc 21
- Eclipse IDE
- Maven
- MySQL Server + MySQL Workbench/phpMyAdmin

## 2. Tạo CSDL
Mở MySQL Workbench -> chạy toàn bộ:
`database/minishop_db.sql`

Nếu MySQL root có mật khẩu, sửa:
`src/main/java/vn/edu/eaut/lab5/config/DBHelper.java`

## 3. Import vào Eclipse
File -> Import -> Maven -> Existing Maven Projects
-> chọn thư mục `lab05-minishop-swing-jdbc`
-> Finish.

Chờ Maven tải `mysql-connector-j 8.4.0`.

## 4. Chạy
Right click:
`src/main/java/vn/edu/eaut/lab5/App.java`
-> Run As -> Java Application.

Tài khoản demo:
- admin / 123
- nhanvien / 123
- ketoan / 123

## 5. Chức năng
- JDBC + PreparedStatement
- Mô hình Model - DAL - BUS - GUI
- CRUD sản phẩm
- CRUD khách hàng + giới hạn SĐT 10 số
- Danh mục sản phẩm
- Kiểm tra và trừ tồn kho khi lập hóa đơn
- Lập hóa đơn + chi tiết + transaction
- Xuất TXT/CSV
- Tìm kiếm sản phẩm nâng cao + phân trang 10 dòng
- Tìm kiếm khách hàng
- Thống kê doanh thu bằng SwingWorker
- Hóa đơn cao nhất / sản phẩm bán chạy
- Đăng nhập + phân quyền ADMIN/NHANVIEN/KETOAN
