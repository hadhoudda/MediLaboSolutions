package com.medilabosolutions.patientservice.exceptions;

public class PatientNotFoundException extends Throwable {
    /**
     * Constructs a new PatientNotFoundException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public PatientNotFoundException(String message) {
        super(message);
    }
}
