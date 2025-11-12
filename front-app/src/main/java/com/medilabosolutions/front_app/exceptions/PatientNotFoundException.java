package com.medilabosolutions.front_app.exceptions;


public class PatientNotFoundException extends RuntimeException {

    public PatientNotFoundException(String message) {
        super(message);
    }

    public PatientNotFoundException() {
        super("Patient non trouvé");
    }
}
