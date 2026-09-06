package vn.edu.eaut.lab14.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    /** Bai 7: trang thong bao khong du quyen truy cap. */
    @GetMapping("/403")
    public String accessDenied() {
        return "error/403";
    }
}
