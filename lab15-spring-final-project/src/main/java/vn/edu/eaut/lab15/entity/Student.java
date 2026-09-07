package vn.edu.eaut.lab15.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Mã sinh viên không được để trống")
    @Column(nullable = false, unique = true, length = 30)
    private String studentCode;

    @NotBlank(message = "Họ tên không được để trống")
    @Column(nullable = false, length = 100)
    private String fullName;

    @Email(message = "Email không hợp lệ")
    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    public Student() {}

    public Student(Long id, String studentCode, String fullName, String email, String phone) {
        this.id = id;
        this.studentCode = studentCode;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
