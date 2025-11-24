package com.medilabosolutions.patientservice.exceptions;

public class PatientNotFoundException extends Throwable {
    public PatientNotFoundException(String message) {
        super(message);
    }
}
