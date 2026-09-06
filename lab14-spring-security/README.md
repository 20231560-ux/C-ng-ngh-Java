# Lab 14 - Bảo mật ứng dụng với Spring Security

Học phần: Công nghệ Java (IT3242) · Package: `vn.edu.eaut.lab14`

## 1. Chạy bằng Eclipse

1. Giải nén thư mục `lab14-spring-security` ra ổ đĩa (đường dẫn không dấu, ví dụ `D:\javaweb\`).
2. Mở Eclipse → **File → Import… → Maven → Existing Maven Projects** → **Next**.
3. **Browse…** tới thư mục `lab14-spring-security` (thư mục có `pom.xml`) → tick project → **Finish**.
4. Chờ Eclipse tải dependency (thanh trạng thái góc dưới phải). Nếu báo lỗi đỏ:
   chuột phải project → **Maven → Update Project…** → tick **Force Update of Snapshots/Releases** → OK.
5. Kiểm tra JDK: chuột phải project → **Properties → Java Build Path → Libraries**,
   JRE System Library phải là **JavaSE-17** trở lên. Nếu chưa có, cài JDK 17/21 rồi
   **Window → Preferences → Java → Installed JREs** để thêm.
6. Chạy: chuột phải `Lab14Application.java` → **Run As → Java Application**
   (hoặc **Run As → Spring Boot App** nếu có Spring Tools 4).
7. Mở trình duyệt: <http://localhost:8080>

Chạy bằng dòng lệnh (nếu đã cài Maven):

```
mvn clean package
mvn spring-boot:run
```

## 2. Tài khoản kiểm thử

| Tài khoản | Mật khẩu | Vai trò | Quyền |
|---|---|---|---|
| admin | 123456 | ADMIN | Xem / thêm / sửa / xóa sinh viên, xem học phần |
| user | 123456 | USER | Chỉ xem danh sách sinh viên |

Tài khoản được lưu trong bảng `app_user` (CSDL H2), mật khẩu mã hóa BCrypt.
Xem dữ liệu tại <http://localhost:8080/h2-console> với JDBC URL `jdbc:h2:mem:lab14db`,
user `sa`, password để trống.

## 3. Ánh xạ bài tập trong đề

| Bài | Yêu cầu | Nơi thực hiện |
|---|---|---|
| 1 | Thêm dependency Spring Security + Thymeleaf Security | `pom.xml` |
| 2 | Cấu hình Security cơ bản | `config/SecurityConfig.java` |
| 3 | User trong bộ nhớ | `SecurityConfig.java` (đoạn chú thích cuối file) |
| 4 | Trang đăng nhập tùy chỉnh | `controller/AuthController.java`, `templates/auth/login.html` |
| 5 | Ẩn/hiện chức năng theo quyền | `templates/students/list.html`, `templates/fragments/layout.html` |
| 6 | `/courses/**` chỉ ADMIN | `SecurityConfig.java` + `controller/CourseController.java` |
| 7 | Trang báo lỗi 403 | `accessDeniedPage("/403")`, `templates/error/403.html` |
| 8 | Menu khác nhau theo vai trò | `templates/fragments/layout.html` |
| 9 | Chỉ ADMIN được xóa sinh viên | `SecurityConfig` + `@PreAuthorize` trong `StudentController` |
| 10 | User lưu trong CSDL | `entity/AppUser`, `repository/AppUserRepository`, `service/CustomUserDetailsService`, `config/DataSeeder` |

> Bài 3 và Bài 10 dùng chung một cơ chế nên chỉ bật được một cái. Mặc định project chạy
> theo Bài 10 (user trong CSDL). Muốn xem Bài 3: xóa `@Service` ở `CustomUserDetailsService`
> và bỏ chú thích khối `userDetailsService(...)` trong `SecurityConfig`.

## 4. Kịch bản chụp ảnh minh chứng

1. Vào <http://localhost:8080> khi chưa đăng nhập → chụp trang chủ.
2. Đăng nhập `admin/123456` → chụp danh sách sinh viên có nút **Thêm / Sửa / Xóa** và menu **Học phần**.
3. Đăng xuất, đăng nhập `user/123456` → chụp danh sách sinh viên **không có** các nút đó.
4. Vẫn ở tài khoản `user`, gõ trực tiếp <http://localhost:8080/students/create>
   hoặc <http://localhost:8080/courses> → chụp trang **403**.
5. Chụp lỗi khi nhập sai mật khẩu ở trang đăng nhập.

Nội dung báo cáo ngắn xem trong `BAO-CAO.md`.
