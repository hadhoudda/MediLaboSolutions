package com.medilabosolutions.back_patient.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_patient")
    private Integer id;

    @NotBlank(message = "Le prénom ne doit pas être vide")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Le nom ne doit pas être vide")
    @Column(nullable = false)
    private String lastName;

    @NotNull(message = "La date de naissance ne peut pas être nulle")
    @Past(message = "La date de naissance doit être antérieure à la date d'aujourd'hui")
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @NotBlank(message = "Le genre ne doit pas être vide")
    @Pattern(regexp = "^[MF]$", message = "Le genre doit être 'M' ou 'F'")
    @Column(length = 1, nullable = false)
    private String gender;

    @Column(length = 255)
    private String address;

    @Pattern(regexp = "^[0-9+\\-\\s]{6,20}$", message = "Numéro de téléphone invalide")
    @Column(name = "telephone_number", length = 20)
    private String telephoneNumber;
}
