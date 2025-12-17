package com.medilabosolutions.frontservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller responsible for authentication-related endpoints.
 *
 * <p>This controller handles the login page display and the post-login
 * redirection logic.</p>
 */
@Controller
public class AuthController {

    /**
     * Displays the custom login page.
     *
     * @return the login page view name
     */
    @GetMapping("/login")
    public String login() {
        return "login-page";
    }

    /**
     * Handles login form submission.
     *
     * <p>After successful authentication, the user is redirected
     * to the patient list page.</p>
     *
     * @return a redirect to the patient list page
     */
    @PostMapping("/login")
    public String patients() {
        return "redirect:/patient-list";
    }
}
