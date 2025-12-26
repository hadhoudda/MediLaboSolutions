package com.medilabosolutions.frontservice.Beans;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class PatientBean {

    private Integer id;
    private String firstName;
    private String lastName;
    @Past(message = "La date de naissance ne peut pas être dans le futur")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    @Pattern(regexp = "^[0-9+\\-\\s]{6,20}$", message = "Numéro de téléphone invalide")
    private String telephoneNumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public @Past(message = "La date de naissance ne peut pas être dans le futur") LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(@Past(message = "La date de naissance ne peut pas être dans le futur") LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }
}