# Lab 9 - Tích hợp JPA: Entity, Repository, Transaction

## 1. Mục tiêu
Chuyển ứng dụng CRUD từ dữ liệu trong bộ nhớ (List) sang tầng lưu trữ thật bằng JPA/Hibernate,
gồm 3 module: **Sinh viên**, **Lớp học**, **Điểm**.

## 2. Cấu trúc project
```
lab09-jpa-repository/
├── pom.xml
├── database.sql
└── src/main/
    ├── java/vn/edu/eaut/lab9/
    │   ├── config/JPAUtil.java              # EntityManagerFactory dùng chung
    │   ├── model/                           # Entity: SinhVien, LopHoc, MonHoc, Diem
    │   ├── repository/
    │   │   ├── BaseRepository.java          # CRUD chung (Bài 8)
    │   │   ├── SinhVienRepository.java      # + search/pagination (Bài 9), check trùng mã (Bài 10)
    │   │   ├── LopHocRepository.java
    │   │   ├── MonHocRepository.java
    │   │   └── DiemRepository.java
    │   ├── service/                         # Validate dữ liệu + gọi Repository
    │   │   ├── SinhVienService.java
    │   │   ├── LopHocService.java
    │   │   └── DiemService.java             # Transaction nhiều thao tác (Bài 11)
    │   ├── controller/                      # Servlet: /sinh-vien, /lop-hoc, /diem
    │   └── listener/DataSeedListener.java   # Seed dữ liệu mẫu (Bài 12)
    ├── resources/META-INF/persistence.xml
    └── webapp/
        ├── index.jsp
        ├── WEB-INF/web.xml
        └── views/{sinhvien,lophoc,diem}/{list,form}.jsp
```

## 3. Cách chạy
1. Tạo CSDL: chạy `database.sql` trong MySQL Workbench (hoặc chỉ cần `CREATE DATABASE lab09_jpa`,
   Hibernate sẽ tự tạo bảng nhờ `hibernate.hbm2ddl.auto=update`).
2. Mở `src/main/resources/META-INF/persistence.xml`, sửa lại `jdbc.user` / `jdbc.password` cho đúng MySQL của bạn.
3. Import project vào Eclipse dạng **Existing Maven Project**.
4. Chuột phải project → **Run As → Run on Server**, chọn Tomcat 10.x.
5. Truy cập: `http://localhost:8080/lab09-jpa-repository/`

## 4. Luồng xử lý JPA/Repository/Transaction
- **JPAUtil** khởi tạo `EntityManagerFactory` một lần duy nhất (singleton) cho toàn ứng dụng.
- Mỗi request tạo một `EntityManager` riêng trong Repository, dùng xong `close()` ngay (try/finally).
- Các thao tác **thêm/sửa/xóa** đều bọc trong `EntityTransaction`:
  ```
  tx.begin();
  ... thao tác ...
  tx.commit();
  // catch RuntimeException -> tx.rollback()
  ```
- **Service** (SinhVienService, LopHocService) chịu trách nhiệm validate (trường bắt buộc, email đúng
  định dạng, mã không trùng) trước khi gọi Repository — tránh việc Controller/JSP thao tác dữ liệu
  trực tiếp.
- **DiemService.themSinhVienVaDiemMacDinh()** minh họa transaction gộp nhiều thao tác: thêm 1 sinh
  viên + tạo điểm mặc định cho tất cả môn học, tất cả trong cùng 1 transaction — nếu bước nào lỗi,
  toàn bộ (kể cả sinh viên) sẽ rollback, không lưu dữ liệu nửa chừng.
- **JPQL** dùng trong: tìm kiếm theo tên/mã/lớp (`SinhVienRepository.search`), phân trang bằng
  `setFirstResult`/`setMaxResults`, và `JOIN FETCH` trong `DiemRepository.findAll()` để tránh lỗi
  LazyInitializationException khi hiển thị JSP.

## 5. Các bài tập đã hoàn thành
| Bài | Nội dung | Trạng thái |
|---|---|---|
| 1 | Cấu hình JPA, JPAUtil | ✅ |
| 2 | Entity SinhVien | ✅ |
| 3 | Repository CRUD | ✅ |
| 4 | update/delete/search JPQL | ✅ |
| 5 | Controller kết nối Repository, hiển thị JSP | ✅ |
| 6 | Entity LopHoc, quan hệ 1-nhiều | ✅ |
| 7 | Entity MonHoc, Diem, xếp loại | ✅ |
| 8 | BaseRepository dùng chung | ✅ |
| 9 | Tìm kiếm + phân trang JPQL | ✅ |
| 10 | Validate: mã trùng, email sai định dạng, trường bắt buộc | ✅ |
| 11 | Transaction nhiều thao tác (thêm SV + điểm mặc định) | ✅ (`DiemService`, chưa gắn nút UI riêng) |
| 12 | Seed dữ liệu mẫu | ✅ (`DataSeedListener`) |
| 13 | Thêm module thứ 2/3 chuyển từ List sang JPA | ✅ (LopHoc, Điểm) |

## 6. Ghi chú / hướng khắc phục nếu lỗi
- Nếu báo lỗi kết nối CSDL: kiểm tra MySQL đã bật, user/password trong `persistence.xml` đúng chưa.
- Nếu 404 khi mở trang: đảm bảo gõ đúng context path `/lab09-jpa-repository/`.
- Nếu lỗi `ClassNotFoundException`/thiếu thư viện khi deploy: Project → Maven → Update Project,
  và kiểm tra "Maven Dependencies" đã có trong Deployment Assembly.
