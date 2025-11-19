package com.medilabosolutions.gateway_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthTestController {

    @GetMapping("/api/auth-test")
    public String authTest() {
        return "OK";
    }
}
