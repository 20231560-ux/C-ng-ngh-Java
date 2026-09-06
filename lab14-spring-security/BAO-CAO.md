# Báo cáo ngắn - Lab 14: Authentication và Authorization

## 1. Authentication (xác thực) - "Bạn là ai?"

Khi người dùng gửi form đăng nhập tới `/login`, chuỗi filter của Spring Security bắt request
và thực hiện:

1. `UsernamePasswordAuthenticationFilter` lấy hai tham số `username`, `password`.
2. `AuthenticationManager` gọi `DaoAuthenticationProvider`.
3. Provider gọi `CustomUserDetailsService.loadUserByUsername()` để tìm tài khoản trong bảng
   `app_user` của CSDL. Không tìm thấy thì ném `UsernameNotFoundException`.
4. Mật khẩu người dùng nhập được `BCryptPasswordEncoder` băm rồi so sánh với chuỗi băm lưu
   trong CSDL. Mật khẩu gốc không bao giờ được lưu.
5. Đúng thì tạo đối tượng `Authentication` chứa username và danh sách quyền, cất vào
   `SecurityContext` của session; sai thì chuyển về `/login?error`.

Cấu hình liên quan trong `SecurityConfig`: `formLogin()` chỉ ra trang đăng nhập riêng
`/login`, trang vào sau khi đăng nhập thành công `/students`, và `logout()` hủy session
rồi quay về trang chủ.

## 2. Authorization (phân quyền) - "Bạn được làm gì?"

Ứng dụng phân quyền ở hai mức:

**Mức URL** - trong `SecurityConfig`:

| Đường dẫn | Quyền yêu cầu |
|---|---|
| `/`, `/about`, `/css/**`, `/login` | công khai |
| `/courses/**` | ROLE_ADMIN |
| `/students/create`, `/students/edit/**`, `/students/delete/**` | ROLE_ADMIN |
| `/students/**` | ROLE_ADMIN hoặc ROLE_USER |
| còn lại | phải đăng nhập |

**Mức phương thức** - `@EnableMethodSecurity` cùng `@PreAuthorize("hasRole('ADMIN')")` đặt
trên các method thêm/sửa/xóa của `StudentController` và trên `CourseController`. Lớp bảo vệ
thứ hai này giúp chức năng vẫn an toàn kể cả khi ai đó sửa nhầm cấu hình URL.

Khi người dùng đã đăng nhập nhưng không đủ quyền, `accessDeniedPage("/403")` đưa họ tới
trang `error/403.html` thay vì trang trắng của server.

## 3. Ẩn/hiện giao diện theo vai trò

Thymeleaf Security (`thymeleaf-extras-springsecurity6`) dùng `sec:authorize` để chỉ vẽ phần
giao diện mà người dùng có quyền: nút Thêm/Sửa/Xóa và menu Học phần chỉ hiện với ADMIN,
`sec:authentication="name"` hiển thị tên tài khoản đang đăng nhập.

Đây chỉ là lớp giao diện. Ẩn nút không có nghĩa là an toàn — người dùng vẫn có thể gõ thẳng
URL. Vì vậy mọi chức năng đã ẩn đều được chặn thêm ở tầng cấu hình URL và tầng method.

## 4. Kết quả kiểm thử

| Kịch bản | Kết quả mong đợi |
|---|---|
| Vào `/` khi chưa đăng nhập | Xem được, không bị chuyển hướng |
| Vào `/students` khi chưa đăng nhập | Bị chuyển sang `/login` |
| Đăng nhập sai mật khẩu | Quay lại `/login?error`, có thông báo |
| admin vào `/students` | Thấy đầy đủ nút Thêm, Sửa, Xóa và menu Học phần |
| user vào `/students` | Chỉ xem danh sách, không thấy các nút |
| user gõ `/students/create` hoặc `/courses` | Hiện trang 403 |
| Bấm Đăng xuất | Session bị hủy, quay về trang chủ |
