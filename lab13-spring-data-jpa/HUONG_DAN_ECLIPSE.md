# Lab 13 – Kết nối CSDL với Spring Data JPA (chạy bằng Eclipse)

## 1. Chuẩn bị
- JDK 17 (hoặc 21). Kiểm tra: `java -version`, `mvn -version`
- Eclipse IDE for Enterprise Java and Web Developers
- (Khuyến nghị) Cài **Spring Tools 4**: Help ▸ Eclipse Marketplace ▸ tìm "Spring Tools 4" ▸ Install

## 2. Import project vào Eclipse
1. Giải nén thư mục `lab13-spring-data-jpa` vào workspace.
2. Eclipse ▸ **File ▸ Import… ▸ Maven ▸ Existing Maven Projects ▸ Next**.
3. **Browse…** tới thư mục `lab13-spring-data-jpa` (thư mục có `pom.xml`) ▸ tick project ▸ **Finish**.
4. Chờ Maven tải dependency (thanh trạng thái góc dưới phải).
5. Nếu báo lỗi đỏ: chuột phải project ▸ **Maven ▸ Update Project…** ▸ tick *Force Update of Snapshots/Releases* ▸ OK.
6. Kiểm tra JDK: chuột phải project ▸ **Properties ▸ Java Build Path ▸ Libraries** → JRE System Library phải là **JavaSE-17**.

## 3. Chạy ứng dụng
Cách 1 (thường dùng): mở `src/main/java/vn/edu/eaut/lab13/Lab13Application.java` ▸ chuột phải ▸ **Run As ▸ Java Application**.

Cách 2 (có Spring Tools): chuột phải project ▸ **Run As ▸ Spring Boot App**.

Cách 3 (Maven): chuột phải project ▸ **Run As ▸ Maven build…** ▸ Goals: `spring-boot:run` ▸ Run.

Console hiện `Started Lab13Application` là chạy thành công.

## 4. Các URL kiểm thử
| URL | Chức năng |
|---|---|
| http://localhost:8080/ | Trang chủ, đếm số bản ghi |
| http://localhost:8080/students | Danh sách sinh viên (Bài 5) |
| http://localhost:8080/students/create | Thêm sinh viên |
| http://localhost:8080/students/edit/1 | Sửa sinh viên (Bài 6) |
| http://localhost:8080/students?keyword=an | Tìm theo họ tên (Bài 7) |
| http://localhost:8080/students/delete/1 | Xóa sinh viên |
| http://localhost:8080/courses | CRUD môn học (Bài 8, 9) |
| http://localhost:8080/h2-console | H2 Console – chụp ảnh bảng `STUDENTS` |

**Đăng nhập H2 Console:** JDBC URL `jdbc:h2:mem:eautdb`, User `sa`, Password để trống ▸ Connect ▸ chạy `SELECT * FROM STUDENTS;` rồi chụp màn hình.

## 5. Bài 10 – Chuyển sang MySQL
1. Trong MySQL Workbench / phpMyAdmin chạy:
   ```sql
   CREATE DATABASE eautdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
2. Mở `src/main/resources/application-mysql.properties`, sửa `username` / `password` cho đúng máy mình.
3. Eclipse ▸ **Run ▸ Run Configurations…** ▸ chọn cấu hình `Lab13Application` ▸ tab **Arguments** ▸ ô *Program arguments* nhập:
   ```
   --spring.profiles.active=mysql
   ```
   ▸ Apply ▸ Run.
4. Ứng dụng chạy lại, Hibernate tự tạo bảng `students`, `courses` trong `eautdb`.
5. Chụp ảnh bảng dữ liệu trong MySQL Workbench để nộp.

> Muốn quay lại H2: xóa program argument đó đi.

## 6. Đối chiếu bài tập với mã nguồn
| Bài | Yêu cầu | File thực hiện |
|---|---|---|
| 1 | Dependency JPA + H2 | `pom.xml` |
| 2 | Entity Student | `entity/Student.java` |
| 3 | Repository | `repository/StudentRepository.java` |
| 4 | Service | `service/StudentService.java` |
| 5 | Controller CRUD | `controller/StudentController.java`, `templates/students/*` |
| 6 | Sửa sinh viên theo id | `StudentController.edit()` + `save()` |
| 7 | Tìm kiếm theo họ tên | `findByFullNameContainingIgnoreCase` + `StudentService.search()` |
| 8 | Entity Course | `entity/Course.java` |
| 9 | CRUD Course | `CourseRepository`, `CourseService`, `CourseController`, `templates/courses/*` |
| 10 | Chuyển sang MySQL | `application-mysql.properties` |

## 7. Nộp bài
Đổi tên thư mục/ZIP theo mẫu: `Lab13_MSSV_HoTen.zip`, kèm:
- Mã nguồn project (có thể xóa thư mục `target/` cho nhẹ)
- Ảnh H2 Console / MySQL có bảng `students`
- Ảnh màn hình CRUD chạy thành công
- File `BAO_CAO.md` (hoặc Word) mô tả Entity – Repository – Service – Controller
