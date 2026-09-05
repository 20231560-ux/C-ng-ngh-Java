package vn.edu.eaut.lab13.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.eaut.lab13.entity.Student;

import java.util.List;
import java.util.Optional;

/**
 * Bai 3: Repository ke thua JpaRepository + method query.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Bai 7: tim kiem theo ho ten (khong phan biet hoa thuong)
    List<Student> findByFullNameContainingIgnoreCase(String keyword);

    boolean existsByStudentCode(String studentCode);

    Optional<Student> findByStudentCode(String studentCode);

    List<Student> findByClassNameIgnoreCase(String className);
}
