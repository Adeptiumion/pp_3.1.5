package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Deprecated
@Controller
@RequestMapping("/auth")
public class AuthController {
    @GetMapping("/login")
    public String login() {
        return "bootstrap/login";
    }
}
