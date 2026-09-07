package vn.edu.eaut.lab15.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.eaut.lab15.service.*;

@Controller
@RequestMapping("/enrollments")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;
    private final StudentService studentService;
    private final CourseService courseService;

    public EnrollmentController(EnrollmentService enrollmentService,
                                StudentService studentService,
                                CourseService courseService) {
        this.enrollmentService = enrollmentService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("enrollments", enrollmentService.findAll());
        return "enrollments/list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("courses", courseService.findAll());
        return "enrollments/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam Long studentId,
                       @RequestParam Long courseId,
                       RedirectAttributes redirect) {
        try {
            enrollmentService.enroll(studentId, courseId);
            redirect.addFlashAttribute("success", "Đăng ký học phần thành công!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/enrollments";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            enrollmentService.cancel(id);
            redirect.addFlashAttribute("success", "Hủy đăng ký thành công!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/enrollments";
    }

    @GetMapping("/student/{studentId}")
    public String byStudent(@PathVariable Long studentId, Model model) {
        model.addAttribute("student", studentService.findById(studentId));
        model.addAttribute("enrollments", enrollmentService.findByStudentId(studentId));
        return "enrollments/student";
    }
}
