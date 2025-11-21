package com.medilabosolutions.frontservice.exceptions;


public class PatientNotFoundException extends RuntimeException {

    public PatientNotFoundException(String message) {
        super(message);
    }

    public PatientNotFoundException() {
        super("Patient non trouvé");
    }
}
