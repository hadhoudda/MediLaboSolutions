package com.medilabosolutions.frontservice.client;

import com.medilabosolutions.frontservice.Beans.NoteBean;
import com.medilabosolutions.frontservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Feign client used to communicate with the Note microservice.
 */
@FeignClient(
        name = "note-service",
        configuration = FeignConfig.class
)
public interface NoteGatewayClient {

    /**
     * Retrieves all notes associated with a given patient.
     *
     * @param patId the patient identifier
     * @return the list of notes for the given patient
     */
    @GetMapping("/api/notes/patient/{patId}")
    List<NoteBean> getNotesByPatientId(@PathVariable("patId") int patId);

    /**
     * Creates a new note.
     *
     * @param noteBean the note to be created
     * @return the created note
     */
    @PostMapping("/api/notes/patient")
    NoteBean createNote(@RequestBody NoteBean noteBean);

    /**
     * Updates an existing note.
     *
     * @param id the note identifier
     * @param noteBean the updated note data
     */
    @PutMapping("/api/notes/{id}")
    void updateNote(@PathVariable String id, @RequestBody NoteBean noteBean);

    /**
     * Deletes a note by its identifier.
     *
     * @param id the note identifier
     */
    @DeleteMapping("/api/notes/{id}")
    void deleteNotePatientById(@PathVariable String id);
}
