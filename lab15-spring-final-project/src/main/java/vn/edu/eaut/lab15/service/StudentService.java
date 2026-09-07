package vn.edu.eaut.lab15.service;

import org.springframework.stereotype.Service;
import vn.edu.eaut.lab15.entity.Student;
import vn.edu.eaut.lab15.repository.StudentRepository;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public List<Student> findAll() { return repository.findAll(); }

    public List<Student> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return findAll();
        return repository.findByStudentCodeContainingIgnoreCaseOrFullNameContainingIgnoreCase(keyword, keyword);
    }

    public Student findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên"));
    }

    public Student save(Student student) { return repository.save(student); }

    public void delete(Long id) { repository.deleteById(id); }

    public long count() { return repository.count(); }
}
