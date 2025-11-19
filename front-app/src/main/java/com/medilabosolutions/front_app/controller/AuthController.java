package com.medilabosolutions.front_app.controller;


import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String showLogin(){

        return "login";
    }

    @PostMapping("/login")
    public String login(String username, String password, HttpSession session) {
        // Ici, tu peux vérifier via la gateway/back ou laisser Spring Security gérer
        session.setAttribute("username", username);
        //session.setAttribute("password", password);
        return "redirect:/front-app/patients";
    }

}
