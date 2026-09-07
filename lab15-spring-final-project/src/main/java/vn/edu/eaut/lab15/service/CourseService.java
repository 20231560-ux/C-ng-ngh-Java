package vn.edu.eaut.lab15.service;

import org.springframework.stereotype.Service;
import vn.edu.eaut.lab15.entity.Course;
import vn.edu.eaut.lab15.repository.CourseRepository;
import java.util.List;

@Service
public class CourseService {
    private final CourseRepository repository;

    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }

    public List<Course> findAll() { return repository.findAll(); }

    public List<Course> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return findAll();
        return repository.findByCourseCodeContainingIgnoreCaseOrCourseNameContainingIgnoreCase(keyword, keyword);
    }

    public Course findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy môn học"));
    }

    public Course save(Course course) { return repository.save(course); }

    public void delete(Long id) { repository.deleteById(id); }

    public long count() { return repository.count(); }
}
