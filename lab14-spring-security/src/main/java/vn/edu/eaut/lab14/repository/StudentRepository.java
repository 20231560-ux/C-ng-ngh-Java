package vn.edu.eaut.lab14.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.edu.eaut.lab14.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
