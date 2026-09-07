package vn.edu.eaut.lab15.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Mã môn học không được để trống")
    @Column(nullable = false, unique = true, length = 30)
    private String courseCode;

    @NotBlank(message = "Tên môn học không được để trống")
    @Column(nullable = false, length = 150)
    private String courseName;

    @Min(value = 1, message = "Số tín chỉ phải lớn hơn 0")
    private Integer credits;

    public Course() {}

    public Course(Long id, String courseCode, String courseName, Integer credits) {
        this.id = id;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }
}
