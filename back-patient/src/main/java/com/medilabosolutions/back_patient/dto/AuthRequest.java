package com.medilabosolutions.back_patient.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    private String username;
    private String password;

}
