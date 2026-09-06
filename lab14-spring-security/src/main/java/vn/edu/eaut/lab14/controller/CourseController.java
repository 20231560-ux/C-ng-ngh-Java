package vn.edu.eaut.lab14.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.edu.eaut.lab14.repository.CourseRepository;

/** Bai 6: toan bo /courses/** chi danh cho ADMIN. */
@Controller
@RequestMapping("/courses")
@PreAuthorize("hasRole('ADMIN')")
public class CourseController {

    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("courses", courseRepository.findAll());
        return "courses/list";
    }
}
