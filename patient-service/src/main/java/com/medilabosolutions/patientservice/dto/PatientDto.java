package com.medilabosolutions.patientservice.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDto {

    private Integer id;

    @NotBlank(message = "Le prénom ne doit pas être vide")
    private String firstName;

    @NotBlank(message = "Le nom ne doit pas être vide")
    private String lastName;

    @NotNull(message = "La date de naissance ne peut pas être nulle")
    @Past(message = "La date de naissance ne peut pas être dans le futur.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Le genre ne doit pas être vide")
    @Pattern(regexp = "^[MF]$", message = "Le genre doit être 'M' ou 'F'")
    private String gender;

    private String address;

    @Pattern(regexp = "^[0-9+\\-\\s]{6,20}$", message = "Numéro de téléphone invalide")
    private String telephoneNumber;
}
