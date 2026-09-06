package vn.edu.eaut.lab14.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ma hoc phan khong duoc de trong")
    @Column(nullable = false, length = 20)
    private String code;

    @NotBlank(message = "Ten hoc phan khong duoc de trong")
    @Column(nullable = false, length = 150)
    private String name;

    private int credits;

    public Course() {
    }

    public Course(String code, String name, int credits) {
        this.code = code;
        this.name = name;
        this.credits = credits;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
}
