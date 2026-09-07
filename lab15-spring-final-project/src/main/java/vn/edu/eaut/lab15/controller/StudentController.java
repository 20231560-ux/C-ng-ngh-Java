package vn.edu.eaut.lab15.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.eaut.lab15.entity.Student;
import vn.edu.eaut.lab15.service.StudentService;

@Controller
@RequestMapping("/students")
public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("students", service.search(keyword));
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        return "students/list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("student", new Student());
        return "students/form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("student", service.findById(id));
        return "students/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("student") Student student,
                       BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) return "students/form";
        service.save(student);
        redirect.addFlashAttribute("success", "Lưu sinh viên thành công!");
        return "redirect:/students";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            service.delete(id);
            redirect.addFlashAttribute("success", "Xóa sinh viên thành công!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Không thể xóa sinh viên này.");
        }
        return "redirect:/students";
    }
}
