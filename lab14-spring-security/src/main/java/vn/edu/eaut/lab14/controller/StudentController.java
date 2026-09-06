package vn.edu.eaut.lab14.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import vn.edu.eaut.lab14.entity.Student;
import vn.edu.eaut.lab14.repository.StudentRepository;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /** ADMIN va USER deu xem duoc danh sach. */
    @GetMapping
    public String list(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        return "students/list";
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("formTitle", "Them sinh vien");
        return "students/form";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String create(@Valid @ModelAttribute("student") Student student,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("formTitle", "Them sinh vien");
            return "students/form";
        }
        studentRepository.save(student);
        redirect.addFlashAttribute("message", "Da them sinh vien " + student.getFullName());
        return "redirect:/students";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model) {
        Student student = studentRepository.findById(id).orElseThrow();
        model.addAttribute("student", student);
        model.addAttribute("formTitle", "Sua sinh vien");
        return "students/form";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute("student") Student student,
                       BindingResult result,
                       Model model,
                       RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("formTitle", "Sua sinh vien");
            return "students/form";
        }
        student.setId(id);
        studentRepository.save(student);
        redirect.addFlashAttribute("message", "Da cap nhat sinh vien " + student.getFullName());
        return "redirect:/students";
    }

    /** Bai 9: chi ADMIN moi duoc xoa sinh vien. */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        studentRepository.deleteById(id);
        redirect.addFlashAttribute("message", "Da xoa sinh vien.");
        return "redirect:/students";
    }
}
