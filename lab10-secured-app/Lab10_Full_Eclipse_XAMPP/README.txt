LAB 10 FULL - ECLIPSE + XAMPP + TOMCAT 10.1
================================================
Theo cấu trúc Lab 10: Login/Logout, Role, Filter bảo vệ URL, quản lý tài khoản,
hồ sơ, đổi mật khẩu, 3 module nghiệp vụ JPA (Sinh viên/Sách/Sản phẩm),
trang lỗi, listener log và dashboard/menu.

CÀI ĐẶT:
1. XAMPP: Start Apache và MySQL.
2. Mở phpMyAdmin -> SQL -> chạy database/lab10_db.sql.
3. Eclipse -> Import -> Maven -> Existing Maven Projects.
4. Chọn thư mục lab10-full.
5. persistence.xml đang để MySQL root KHÔNG MẬT KHẨU. Nếu XAMPP của bạn có password,
   sửa jakarta.persistence.jdbc.password.
6. Maven -> Update Project.
7. Tomcat 10.1 -> Run on Server.

URL: http://localhost:8080/lab10-full/

Tài khoản:
admin@gmail.com / 123456
staff@gmail.com / 123456
user@gmail.com / 123456

Phân quyền:
ADMIN: /admin/* và /staff/*
STAFF: /staff/*
USER: /user/*
Chưa đăng nhập: bị chuyển về login.

3 module JPA:
- Sinh viên
- Sách
- Sản phẩm

Lưu ý: mật khẩu plain text chỉ để phục vụ bài thực hành; hệ thống thật nên băm mật khẩu.
