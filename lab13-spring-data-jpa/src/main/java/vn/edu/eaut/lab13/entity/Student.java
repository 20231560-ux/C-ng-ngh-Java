package vn.edu.eaut.lab13.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Bai 2: Entity Student anh xa bang students.
 */
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ma sinh vien khong duoc de trong")
    @Column(name = "student_code", nullable = false, unique = true, length = 20)
    private String studentCode;

    @NotBlank(message = "Ho ten khong duoc de trong")
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Email(message = "Email khong hop le")
    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "class_name", length = 50)
    private String className;

    public Student() {
    }

    public Student(String studentCode, String fullName, String email, String className) {
        this.studentCode = studentCode;
        this.fullName = fullName;
        this.email = email;
        this.className = className;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    @Override
    public String toString() {
        return "Student{id=" + id + ", studentCode='" + studentCode + "', fullName='" + fullName + "'}";
    }
}
