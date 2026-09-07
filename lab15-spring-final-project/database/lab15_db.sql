CREATE DATABASE IF NOT EXISTS lab15_db
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE lab15_db;

CREATE TABLE IF NOT EXISTS students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_code VARCHAR(30) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(30) NOT NULL UNIQUE,
    course_name VARCHAR(150) NOT NULL,
    credits INT
);

CREATE TABLE IF NOT EXISTS enrollments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    enroll_date DATE,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    CONSTRAINT fk_enrollment_student FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT fk_enrollment_course FOREIGN KEY (course_id) REFERENCES courses(id),
    CONSTRAINT uk_student_course UNIQUE (student_id, course_id)
);

INSERT INTO students(student_code, full_name, email, phone) VALUES
('SV001', 'Nguyễn Văn An', 'an@example.com', '0900000001'),
('SV002', 'Trần Thị Bình', 'binh@example.com', '0900000002');

INSERT INTO courses(course_code, course_name, credits) VALUES
('JAVA01', 'Công nghệ Java', 3),
('WEB01', 'Lập trình Web', 3),
('DB01', 'Cơ sở dữ liệu', 3);
