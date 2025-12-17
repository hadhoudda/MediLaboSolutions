package com.medilabosolutions.riskservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientDto {

    private Integer id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;

}
