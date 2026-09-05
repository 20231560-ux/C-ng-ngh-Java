# BÁO CÁO LAB 13 – KẾT NỐI CSDL VỚI SPRING DATA JPA

**Học phần:** Công nghệ Java (IT3242) — **Chương 4:** Phát triển ứng dụng với Spring Framework
**Sinh viên:** ……………………… **MSSV:** ……………… **Lớp:** ………………
**Project:** `lab13-spring-data-jpa` — **IDE:** Eclipse

## 1. Cấu hình cơ sở dữ liệu
Dùng H2 in-memory (`jdbc:h2:mem:eautdb`), `spring.jpa.hibernate.ddl-auto=update` để Hibernate tự sinh bảng từ entity, bật `show-sql` để quan sát câu lệnh SQL và bật H2 Console tại `/h2-console`. Cấu hình MySQL được tách riêng trong `application-mysql.properties` và kích hoạt bằng profile `mysql` (Bài 10).

## 2. Tầng Entity
- `Student` ánh xạ bảng `students`: `id` khóa chính tự tăng (`GenerationType.IDENTITY`), `student_code` NOT NULL + UNIQUE, `full_name` NOT NULL, `email`, `class_name`. Ràng buộc dữ liệu nhập bằng `@NotBlank`, `@Email`.
- `Course` ánh xạ bảng `courses`: `course_code`, `course_name`, `credits` (`@Min(1)`).

## 3. Tầng Repository
`StudentRepository extends JpaRepository<Student, Long>` nên đã có sẵn `findAll`, `findById`, `save`, `deleteById`, `count`… mà không phải viết SQL. Bổ sung các **method query** Spring Data tự sinh câu truy vấn theo tên:
- `findByFullNameContainingIgnoreCase(String keyword)` → tìm kiếm gần đúng theo họ tên (Bài 7).
- `existsByStudentCode`, `findByStudentCode` → kiểm tra trùng mã sinh viên.
`CourseRepository` tương tự cho môn học.

## 4. Tầng Service
`StudentService` chứa nghiệp vụ và là nơi đặt `@Transactional`: lấy danh sách, tìm theo id (ném lỗi khi không tồn tại), tìm kiếm theo từ khóa (từ khóa rỗng thì trả về toàn bộ), kiểm tra trùng mã khi thêm/sửa, lưu và xóa. Controller không gọi thẳng Repository mà đi qua Service đúng mô hình nhiều tầng.

## 5. Tầng Controller và giao diện
`StudentController` ánh xạ `/students`:
| Method | URL | Chức năng |
|---|---|---|
| GET | `/students` | Danh sách + tìm kiếm theo `keyword` |
| GET | `/students/create` | Form thêm |
| GET | `/students/edit/{id}` | Form sửa (Bài 6) |
| POST | `/students/save` | Lưu thêm/sửa, có kiểm tra dữ liệu |
| GET | `/students/delete/{id}` | Xóa |

Giao diện dùng Thymeleaf: `templates/students/list.html` (bảng dữ liệu, ô tìm kiếm, nút Sửa/Xóa) và `templates/students/form.html` (dùng chung cho thêm và sửa nhờ trường ẩn `id`). `CourseController` có cấu trúc tương tự cho môn học.

## 6. Kết quả kiểm thử
| Chức năng | URL | Kết quả | Ảnh minh chứng |
|---|---|---|---|
| Danh sách sinh viên | `/students` | Đạt | Hình 1 |
| Thêm sinh viên | `/students/create` | Đạt | Hình 2 |
| Sửa sinh viên | `/students/edit/1` | Đạt | Hình 3 |
| Xóa sinh viên | `/students/delete/{id}` | Đạt | Hình 4 |
| Tìm kiếm theo tên | `/students?keyword=an` | Đạt | Hình 5 |
| CRUD môn học | `/courses` | Đạt | Hình 6 |
| Bảng dữ liệu | `/h2-console` hoặc MySQL | Đạt | Hình 7 |

## 7. Khó khăn và cách khắc phục
- ……………………………………………………………………
- ……………………………………………………………………

## 8. Kết luận
Bài lab đã cấu hình được kết nối CSDL trong Spring Boot, ánh xạ entity bằng annotation JPA, sử dụng Spring Data JPA Repository để giảm mã lặp, tách rõ ba tầng Controller – Service – Repository và thực hiện CRUD, tìm kiếm trên cơ sở dữ liệu thật.
