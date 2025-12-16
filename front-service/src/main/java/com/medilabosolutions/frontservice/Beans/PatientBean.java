package com.medilabosolutions.frontservice.Beans;

import jakarta.validation.constraints.Past;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
public class PatientBean {

    private Integer id;
    private String firstName;
    private String lastName;
    @Past(message = "La date de naissance doit être dans le passé")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String telephoneNumber;

}