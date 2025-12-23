package com.medilabosolutions.noteservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabosolutions.noteservice.dto.NoteDto;
import com.medilabosolutions.noteservice.model.Note;
import com.medilabosolutions.noteservice.repository.NoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the NoteController.
 *
 * <p>Tests the API endpoints for retrieving, creating, updating, and deleting notes.
 * Uses MockMvc with a mock security context for authentication.</p>
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class NoteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Note testNote;

    /**
     * Set up the database before each test.
     * Creates a sample note for testing.
     */
    @BeforeEach
    void setUp() {
        noteRepository.deleteAll();

        testNote = new Note();
        testNote.setPatId(1);
        testNote.setNote("Note de test");
        testNote.setCreatedNoteDate(Instant.now());
        testNote.setUpdatedNoteDate(Instant.now());

        testNote = noteRepository.save(testNote);
    }

    /**
     * Clean up the database after each test.
     */
    @AfterEach
    void tearDown() {
        noteRepository.deleteAll();
    }

    /**
     * Test retrieving all notes for an existing patient.
     * Expects HTTP 200 and the correct note content.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testGetAllNotesByPatientId() throws Exception {
        mockMvc.perform(get("/api/notes/patient/{patId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].note").value("Note de test"));
    }

    /**
     * Test creating a new note for a patient.
     * Expects HTTP 201 and the correct note content in the response.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testCreateNote() throws Exception {
        NoteDto noteDto = new NoteDto();
        noteDto.setPatId(2);
        noteDto.setNote("Nouvelle note");

        mockMvc.perform(post("/api/notes/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noteDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.note").value("Nouvelle note"));
    }

    /**
     * Test updating an existing note.
     * Expects HTTP 200 and the updated note content.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testUpdateNote() throws Exception {
        String updatedText = "Note mise à jour";

        mockMvc.perform(put("/api/notes/{id}", testNote.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"note\":\"" + updatedText + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.note").value(updatedText));
    }

    /**
     * Test deleting an existing note.
     * Expects HTTP 200 and confirmation message.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testDeleteNote() throws Exception {
        mockMvc.perform(delete("/api/notes/{id}", testNote.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Note deleted"));
    }

    /**
     * Test retrieving notes for a non-existing patient.
     * Expects HTTP 404 with appropriate error message.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testGetNotesForNonExistingPatient() throws Exception {
        mockMvc.perform(get("/api/notes/patient/{patId}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("No notes found for patient with id: 999 or patient does not exist."));
    }

}
