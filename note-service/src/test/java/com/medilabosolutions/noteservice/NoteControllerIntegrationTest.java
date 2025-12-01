package com.medilabosolutions.noteservice;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class NoteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Note testNote;

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

    @AfterEach
    void tearDown() {
        noteRepository.deleteAll();
    }

    @Test
    void testGetAllNotesByPatientId() throws Exception {
        mockMvc.perform(get("/api/notes/patient/{patId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].note").value("Note de test"));
    }

    @Test
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

    @Test
    void testUpdateNote() throws Exception {
        String updatedText = "Note mise à jour";

        mockMvc.perform(put("/api/notes/{id}", testNote.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"note\":\"" + updatedText + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.note").value(updatedText));
    }

    @Test
    void testDeleteNote() throws Exception {
        mockMvc.perform(delete("/api/notes/{id}", testNote.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Note supprimée"));
    }

    @Test
    void testGetNotesForNonExistingPatient() throws Exception {
        mockMvc.perform(get("/api/notes/patient/{patId}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Aucune note trouvée pour le patient avec l'id : 999 ou le patient n'existe pas."));
    }
}
