package com.medilabosolutions.patientservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PatientNotFoundException extends RuntimeException {
    /**
     * Constructs a new PatientNotFoundException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public PatientNotFoundException(String message) {
        super(message);
    }
}
