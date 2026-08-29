package vn.edu.eaut.lab12.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vn.edu.eaut.lab12.model.Student;
import vn.edu.eaut.lab12.service.StudentService;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Bài 3: danh sách sinh viên
    // Bài 9: tìm kiếm theo họ tên (dùng chung param "keyword", có thể để trống)
    @GetMapping
    public String list(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Student> students = (keyword == null || keyword.isBlank())
                ? studentService.findAll()
                : studentService.searchByName(keyword);
        model.addAttribute("students", students);
        model.addAttribute("keyword", keyword);
        return "students/list";
    }

    // Bài 4: form thêm sinh viên
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("student", new Student());
        return "students/form";
    }

    // Bài 7: form sửa sinh viên
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Student student = studentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sinh viên id = " + id));
        model.addAttribute("student", student);
        return "students/form";
    }

    // Bài 5 + Bài 10: lưu (thêm mới hoặc cập nhật) kèm validate + kiểm tra trùng mã sinh viên
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("student") Student student,
                        BindingResult result) {

        // Bài 10: kiểm tra trùng mã sinh viên (bỏ qua chính bản ghi đang sửa)
        if (studentService.existsByStudentCode(student.getStudentCode(), student.getId())) {
            result.rejectValue("studentCode", "duplicate", "Mã sinh viên đã tồn tại trong danh sách");
        }

        if (result.hasErrors()) {
            return "students/form";
        }

        studentService.save(student);
        return "redirect:/students";
    }

    // Bài 6: xem chi tiết sinh viên theo id
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Student student = studentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sinh viên id = " + id));
        model.addAttribute("student", student);
        return "students/detail";
    }

    // Bài 8: xóa sinh viên khỏi danh sách
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        studentService.deleteById(id);
        return "redirect:/students";
    }
}
