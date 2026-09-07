LAB 15 - SPRING FRAMEWORK - ECLIPSE

1. Yêu cầu:
- JDK 17
- Maven 3.x
- Eclipse IDE có hỗ trợ Maven/Spring
- MySQL 8.x

2. Tạo CSDL:
- Mở MySQL Workbench hoặc phpMyAdmin.
- Chạy file database/lab15_db.sql
- Mặc định ứng dụng dùng:
  database: lab15_db
  username: root
  password: (trống)

Nếu MySQL của bạn có mật khẩu, mở:
src/main/resources/application.properties
và sửa:
spring.datasource.password=MAT_KHAU_MYSQL

3. Import vào Eclipse:
- File -> Import
- Maven -> Existing Maven Projects
- Chọn thư mục lab15-spring-final-project
- Finish
- Chờ Maven tải dependency.
- Chuột phải project -> Maven -> Update Project.

4. Chạy:
- Mở Lab15Application.java
- Run As -> Java Application hoặc Spring Boot App.

Hoặc Terminal:
mvn clean package
mvn spring-boot:run

5. Truy cập:
http://localhost:8080

6. Tài khoản demo:
ADMIN:
username: admin
password: 123456

USER:
username: user
password: 123456

7. Chức năng:
- Dashboard thống kê sinh viên, môn học, lượt đăng ký
- CRUD sinh viên
- CRUD môn học
- Đăng ký học phần
- Kiểm tra đăng ký trùng
- Hủy đăng ký
- Xem môn học đã đăng ký của một sinh viên
- Login/logout
- Phân quyền USER/ADMIN ở mức Spring Security.

Lưu ý:
Đây là project bám theo các yêu cầu Lab 15 trong đề: Controller-Service-Repository-Entity,
CRUD sinh viên và khóa học, đăng ký học phần, Security/phân quyền, dashboard và giao diện.
