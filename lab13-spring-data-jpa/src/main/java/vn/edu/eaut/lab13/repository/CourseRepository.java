package vn.edu.eaut.lab13.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.eaut.lab13.entity.Course;

import java.util.List;

/**
 * Bai 9: Repository cho Course.
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByCourseNameContainingIgnoreCase(String keyword);

    boolean existsByCourseCode(String courseCode);
}
