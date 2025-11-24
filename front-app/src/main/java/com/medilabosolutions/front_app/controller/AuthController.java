package com.medilabosolutions.front_app.controller;

import com.medilabosolutions.front_app.Beans.AuthRequest;
import com.medilabosolutions.front_app.proxies.MicroservicePatientProxy;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final MicroservicePatientProxy patientProxy;

    public AuthController(MicroservicePatientProxy patientProxy) {
        this.patientProxy = patientProxy;
    }

    // Page de login
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // Traitement du login
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        try {
            AuthRequest authRequest = new AuthRequest();
            authRequest.setUsername(username);
            authRequest.setPassword(password);

            String token = patientProxy.login(authRequest).getBody();
            session.setAttribute("jwt", "Bearer " + token);

            return "redirect:/patients"; // Redirection vers la liste
        } catch (Exception e) {
            model.addAttribute("error", "Login échoué");
            return "login";
        }
    }
}
