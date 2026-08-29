package vn.edu.eaut.lab12.service;

import org.springframework.stereotype.Service;
import vn.edu.eaut.lab12.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final List<Student> students = new ArrayList<>();
    private long nextId = 1;

    public StudentService() {
        // Dữ liệu mẫu để test cho tiện
        students.add(new Student(nextId++, "SV001", "Nguyễn Văn A", "a.nguyen@eaut.edu.vn", "CNTT01"));
        students.add(new Student(nextId++, "SV002", "Trần Thị B", "b.tran@eaut.edu.vn", "CNTT02"));
        students.add(new Student(nextId++, "SV003", "Lê Văn C", "c.le@eaut.edu.vn", "CNTT01"));
    }

    // Bài 2: lấy tất cả sinh viên
    public List<Student> findAll() {
        return students;
    }

    // Bài 2 (mở rộng): thêm mới hoặc cập nhật
    public void save(Student student) {
        if (student.getId() == null) {
            student.setId(nextId++);
            students.add(student);
        } else {
            // Bài 7: cập nhật sinh viên đã tồn tại
            for (int i = 0; i < students.size(); i++) {
                if (students.get(i).getId().equals(student.getId())) {
                    students.set(i, student);
                    return;
                }
            }
            // Nếu không tìm thấy id thì thêm mới (phòng hờ)
            students.add(student);
        }
    }

    // Bài 6: xem chi tiết theo id
    public Optional<Student> findById(Long id) {
        return students.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }

    // Bài 8: xóa sinh viên khỏi danh sách
    public void deleteById(Long id) {
        students.removeIf(s -> s.getId().equals(id));
    }

    // Bài 9: tìm kiếm theo họ tên (không phân biệt hoa thường, tìm gần đúng)
    public List<Student> searchByName(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAll();
        }
        String lower = keyword.trim().toLowerCase();
        List<Student> result = new ArrayList<>();
        for (Student s : students) {
            if (s.getFullName() != null && s.getFullName().toLowerCase().contains(lower)) {
                result.add(s);
            }
        }
        return result;
    }

    // Bài 10: kiểm tra mã sinh viên đã tồn tại chưa (loại trừ chính bản ghi đang sửa)
    public boolean existsByStudentCode(String studentCode, Long excludeId) {
        for (Student s : students) {
            if (s.getStudentCode() != null
                    && s.getStudentCode().equalsIgnoreCase(studentCode)
                    && (excludeId == null || !s.getId().equals(excludeId))) {
                return true;
            }
        }
        return false;
    }
}
