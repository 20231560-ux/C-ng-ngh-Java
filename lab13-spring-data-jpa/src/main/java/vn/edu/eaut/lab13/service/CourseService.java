package vn.edu.eaut.lab13.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.eaut.lab13.entity.Course;
import vn.edu.eaut.lab13.repository.CourseRepository;

import java.util.List;

/**
 * Bai 9: Service cho Course.
 */
@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional(readOnly = true)
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Course findById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Khong tim thay mon hoc co id = " + id));
    }

    @Transactional(readOnly = true)
    public List<Course> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return courseRepository.findAll();
        }
        return courseRepository.findByCourseNameContainingIgnoreCase(keyword.trim());
    }

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public void deleteById(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Khong tim thay mon hoc co id = " + id);
        }
        courseRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long count() {
        return courseRepository.count();
    }
}
