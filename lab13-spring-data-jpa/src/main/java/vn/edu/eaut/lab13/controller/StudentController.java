package vn.edu.eaut.lab13.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.eaut.lab13.entity.Student;
import vn.edu.eaut.lab13.service.StudentService;

/**
 * Bai 5: CRUD sinh vien voi CSDL.
 * Bai 6: sua sinh vien theo id.
 * Bai 7: tim kiem theo ho ten.
 */
@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String list(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        model.addAttribute("students", studentService.search(keyword));
        model.addAttribute("keyword", keyword);
        return "students/list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("pageTitle", "Them sinh vien");
        return "students/form";
    }

    /** Bai 6: mo form sua theo id. */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("student", studentService.findById(id));
            model.addAttribute("pageTitle", "Sua sinh vien");
            return "students/form";
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/students";
        }
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("student") Student student,
                       BindingResult result,
                       Model model,
                       RedirectAttributes ra) {

        if (studentService.isDuplicatedCode(student)) {
            result.rejectValue("studentCode", "duplicate", "Ma sinh vien da ton tai");
        }

        if (result.hasErrors()) {
            model.addAttribute("pageTitle", student.getId() == null ? "Them sinh vien" : "Sua sinh vien");
            return "students/form";
        }

        boolean isNew = student.getId() == null;
        studentService.save(student);
        ra.addFlashAttribute("message", isNew ? "Them sinh vien thanh cong" : "Cap nhat sinh vien thanh cong");
        return "redirect:/students";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            studentService.deleteById(id);
            ra.addFlashAttribute("message", "Xoa sinh vien thanh cong");
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/students";
    }
}
