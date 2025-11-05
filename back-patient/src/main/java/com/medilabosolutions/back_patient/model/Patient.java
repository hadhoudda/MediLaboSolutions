package com.medilabosolutions.back_patient.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;


@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_patient")
    private Integer id;

    @NotNull(message = "firstName can not be null")
    private String firstName;

    @NotNull(message = "latName can not be null")
    private String lastName;

    @NotNull(message = "dateOfBirth can not be null")
    private Date dateOfBirth;

    @NotNull(message = "gender can not be null")
    private String gender;

    private String address;

    @Column(name = "telephone")
    private String telephoneNumber;

}
