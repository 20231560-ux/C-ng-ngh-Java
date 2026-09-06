package vn.edu.eaut.lab14.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ma sinh vien khong duoc de trong")
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String code;

    @NotBlank(message = "Ho ten khong duoc de trong")
    @Column(nullable = false, length = 100)
    private String fullName;

    @Email(message = "Email khong hop le")
    @Column(length = 100)
    private String email;

    @Column(name = "class_name", length = 100)
    private String className;

    public Student() {
    }

    public Student(String code, String fullName, String email, String className) {
        this.code = code;
        this.fullName = fullName;
        this.email = email;
        this.className = className;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
}
