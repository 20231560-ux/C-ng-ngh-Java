package vn.edu.eaut.lab15.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.edu.eaut.lab15.service.*;

@Controller
public class DashboardController {
    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    public DashboardController(StudentService studentService,
                               CourseService courseService,
                               EnrollmentService enrollmentService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("studentCount", studentService.count());
        model.addAttribute("courseCount", courseService.count());
        model.addAttribute("enrollmentCount", enrollmentService.count());
        return "dashboard";
    }
}
