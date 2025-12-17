package com.medilabosolutions.noteservice.exceptions;

public class NoteNotFoundException extends Throwable {
    /**
     * Constructs a new NoteNotFoundException with the specified detail message.
     *
     * @param message the detail message describing the error
     */
    public NoteNotFoundException(String message) {
        super(message);
    }

}
