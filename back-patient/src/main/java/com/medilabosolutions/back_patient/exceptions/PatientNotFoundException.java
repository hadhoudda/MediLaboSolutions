package com.medilabosolutions.back_patient.exceptions;

public class PatientNotFoundException extends Throwable {
    public PatientNotFoundException(String message) {
        super(message);
    }
}
