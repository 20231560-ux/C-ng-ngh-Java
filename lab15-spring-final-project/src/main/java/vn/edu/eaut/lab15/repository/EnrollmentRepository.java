package vn.edu.eaut.lab15.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.eaut.lab15.entity.Enrollment;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentId(Long studentId);
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    long countByStudentId(Long studentId);
}
