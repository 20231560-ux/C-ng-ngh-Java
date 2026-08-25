LAB 10 - FULL SOURCE CODE FOR ECLIPSE
======================================

Import:
1. Eclipse -> File -> Import
2. Maven -> Existing Maven Projects
3. Chọn thư mục lab10-secured-app
4. Maven -> Update Project
5. Cấu hình Tomcat 10.1
6. Run on Server

Database:
- Mở database/lab10_db.sql trong MySQL Workbench và chạy.
- Mở src/main/resources/META-INF/persistence.xml
- Đổi value của jakarta.persistence.jdbc.password thành mật khẩu MySQL trên máy bạn.

Nếu MySQL của bạn không có mật khẩu:
value=""

URL:
http://localhost:8080/lab10-secured-app/

Tài khoản:
ADMIN: admin@gmail.com / 123456
STAFF: staff@gmail.com / 123456
USER : user@gmail.com / 123456

Các phần có trong source:
- User + Role
- JPA/Hibernate
- UserRepository
- AuthService
- AuthController
- Login/Logout
- HttpSession
- AuthenticationFilter
- AuthorizationFilter
- Dashboard
- ADMIN/STAFF/USER
- 403/404/500
- web.xml
- MySQL script
