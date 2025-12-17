package com.medilabosolutions.frontservice.Beans;

import jakarta.validation.constraints.Past;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class PatientBean {

    private Integer id;
    private String firstName;
    private String lastName;
    @Past(message = "La date de naissance ne peut pas être dans le futur")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String telephoneNumber;

}