package com.medilabosolutions.frontservice.exceptions;

/**
 * Custom exception thrown when a patient is not found in the system.
 *
 * <p>This exception extends {@link RuntimeException} and can be used
 * throughout the application to indicate missing patient data.</p>
 */
public class PatientNotFoundException extends RuntimeException {

    /**
     * Constructs a new exception with a specific message.
     *
     * @param message the detail message
     */
    public PatientNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with a default message.
     */
    public PatientNotFoundException() {
        super("Patient non trouvé");
    }
}
