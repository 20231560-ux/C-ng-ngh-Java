package vn.edu.eaut.lab15.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.eaut.lab15.entity.Course;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByCourseCodeContainingIgnoreCaseOrCourseNameContainingIgnoreCase(
            String courseCode, String courseName);
}
