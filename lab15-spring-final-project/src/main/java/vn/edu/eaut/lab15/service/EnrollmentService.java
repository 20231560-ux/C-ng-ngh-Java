package vn.edu.eaut.lab15.service;

import org.springframework.stereotype.Service;
import vn.edu.eaut.lab15.entity.*;
import vn.edu.eaut.lab15.repository.*;
import java.time.LocalDate;
import java.util.List;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             StudentRepository studentRepository,
                             CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public List<Enrollment> findAll() { return enrollmentRepository.findAll(); }

    public List<Enrollment> findByStudentId(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public void enroll(Long studentId, Long courseId) {
        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new RuntimeException("Sinh viên đã đăng ký môn học này");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy môn học"));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollDate(LocalDate.now());
        enrollmentRepository.save(enrollment);
    }

    public void cancel(Long id) {
        if (!enrollmentRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy đăng ký");
        }
        enrollmentRepository.deleteById(id);
    }

    public long count() { return enrollmentRepository.count(); }
}
