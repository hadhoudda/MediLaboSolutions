package com.medilabosolutions.riskservice.client;

import com.medilabosolutions.riskservice.config.FeignConfig;
import com.medilabosolutions.riskservice.dto.NoteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Feign client for communicating with the Note Service.
 * <p>
 * This client provides access to patient notes for risk assessment purposes.
 */
@FeignClient(
        name = "note-service",
        configuration = FeignConfig.class
)
public interface NoteClient {

    /**
     * Retrieves a list of notes for a specific patient.
     *
     * @param patId The ID of the patient
     * @return List of {@link NoteDto} objects
     */
    @GetMapping("/api/notes/patient/{patId}")
    List<NoteDto> getNotesByPatientId(@PathVariable("patId") int patId);

}
