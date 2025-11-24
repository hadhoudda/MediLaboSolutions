package com.medilabosolutions.frontservice.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login"; // page Thymeleaf
    }

    @PostMapping("/login")
    public String patients() {
//        return "patients"; // page protégée
        return "redirect:/patients";
    }
}
//@Controller
//public class AuthController {
//
//    private static final String VALID_USERNAME = "user";
//    private static final String VALID_PASSWORD = "1234";
//
//    @GetMapping("/login")
//    public String showLogin() {
//        return "login";
//    }
//
//    @PostMapping("/login")
//    public String login(String username, String password, Model model, HttpSession session) {
//
//        if (VALID_USERNAME.equals(username) && VALID_PASSWORD.equals(password)) {
//            // Stocke l'état de connexion dans la session
//            session.setAttribute("isLogged", true);
//            return "redirect:/patients";
//        }
//
//        // Erreur login
//        model.addAttribute("error", true);
//        return "/login";
//    }
//
//    @PostMapping("/logout")
//    public String logout(HttpSession session) {
//        session.invalidate(); // Supprime toutes les données de session
//        return "redirect:/front-app/login";
//    }
//}