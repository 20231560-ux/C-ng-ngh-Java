package vn.edu.eaut.lab15.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.eaut.lab15.entity.Course;
import vn.edu.eaut.lab15.service.CourseService;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private final CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("courses", service.search(keyword));
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        return "courses/list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("course", new Course());
        return "courses/form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("course", service.findById(id));
        return "courses/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("course") Course course,
                       BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) return "courses/form";
        service.save(course);
        redirect.addFlashAttribute("success", "Lưu môn học thành công!");
        return "redirect:/courses";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            service.delete(id);
            redirect.addFlashAttribute("success", "Xóa môn học thành công!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Không thể xóa môn học đang có đăng ký.");
        }
        return "redirect:/courses";
    }
}
