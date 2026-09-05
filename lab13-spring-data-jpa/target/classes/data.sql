-- Dữ liệu mẫu, chạy sau khi Hibernate tạo bảng (H2 in-memory tạo mới mỗi lần chạy)
INSERT INTO students (student_code, full_name, email, class_name) VALUES
 ('SV001', 'Nguyễn Văn An',  'an.nv@eaut.edu.vn',    'DHCNTT01'),
 ('SV002', 'Trần Thị Bình',  'binh.tt@eaut.edu.vn',  'DHCNTT01'),
 ('SV003', 'Lê Văn Cường',   'cuong.lv@eaut.edu.vn', 'DHCNTT02');

INSERT INTO courses (course_code, course_name, credits) VALUES
 ('IT3242', 'Công nghệ Java', 3),
 ('IT3010', 'Cơ sở dữ liệu',  3);
