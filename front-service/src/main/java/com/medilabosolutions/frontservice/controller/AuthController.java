package com.medilabosolutions.frontservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login-page";
    }

    @PostMapping("/login")
    public String patients() {
        return "redirect:/patient-list";
    }
}
