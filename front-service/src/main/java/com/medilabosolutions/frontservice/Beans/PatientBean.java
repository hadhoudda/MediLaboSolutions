package com.medilabosolutions.frontservice.Beans;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PatientBean {
    private Integer id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String telephoneNumber;
}