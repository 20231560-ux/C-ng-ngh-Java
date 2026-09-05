package vn.edu.eaut.lab13.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.eaut.lab13.entity.Student;
import vn.edu.eaut.lab13.repository.StudentRepository;

import java.util.List;

/**
 * Bai 4: Service xu ly nghiep vu, goi Repository.
 */
@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Student findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Khong tim thay sinh vien co id = " + id));
    }

    /** Bai 7: tim kiem theo ho ten. */
    @Transactional(readOnly = true)
    public List<Student> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return studentRepository.findAll();
        }
        return studentRepository.findByFullNameContainingIgnoreCase(keyword.trim());
    }

    /** Kiem tra trung ma sinh vien (bo qua chinh no khi sua - Bai 6). */
    @Transactional(readOnly = true)
    public boolean isDuplicatedCode(Student student) {
        return studentRepository.findByStudentCode(student.getStudentCode())
                .filter(existing -> !existing.getId().equals(student.getId()))
                .isPresent();
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public void deleteById(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Khong tim thay sinh vien co id = " + id);
        }
        studentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long count() {
        return studentRepository.count();
    }
}
