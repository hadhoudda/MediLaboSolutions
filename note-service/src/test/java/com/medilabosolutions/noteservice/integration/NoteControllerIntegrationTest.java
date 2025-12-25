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
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testGetAllNotesByPatientId() throws Exception {
        // Given
        int patId = 1;

        // When
        mockMvc.perform(get("/api/notes/patient/{patId}", patId))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].note").value("Note de test"));
    }

    /**
     * Test creating a new note for a patient.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testCreateNote() throws Exception {
        // Given
        NoteDto noteDto = new NoteDto();
        noteDto.setPatId(2);
        noteDto.setNote("Nouvelle note");

        // When
        mockMvc.perform(post("/api/notes/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noteDto)))
                // Then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.note").value("Nouvelle note"));
    }

    /**
     * Test updating an existing note.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testUpdateNote() throws Exception {
        // Given
        String updatedText = "Note mise à jour";
        String noteId = testNote.getId();

        // When
        mockMvc.perform(put("/api/notes/{id}", noteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"note\":\"" + updatedText + "\"}"))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.note").value(updatedText));
    }

    /**
     * Test deleting an existing note.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testDeleteNote() throws Exception {
        // Given
        String noteId = testNote.getId();

        // When
        mockMvc.perform(delete("/api/notes/{id}", noteId))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().string("Note deleted"));
    }

    /**
     * Test retrieving notes for a non-existing patient.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testGetNotesForNonExistingPatient() throws Exception {
        // Given
        int nonExistingPatId = 999;

        // When
        mockMvc.perform(get("/api/notes/patient/{patId}", nonExistingPatId))
                // Then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("No notes found for patient with id: 999 or patient does not exist."));
    }

}
