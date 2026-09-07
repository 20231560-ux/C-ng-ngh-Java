package vn.edu.eaut.lab15.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.eaut.lab15.entity.Student;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByStudentCodeContainingIgnoreCaseOrFullNameContainingIgnoreCase(
            String studentCode, String fullName);
}
