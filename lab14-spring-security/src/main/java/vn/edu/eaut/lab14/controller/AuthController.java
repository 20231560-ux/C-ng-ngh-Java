package vn.edu.eaut.lab14.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** Bai 4: hien thi form dang nhap tuy chinh. */
@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
}
