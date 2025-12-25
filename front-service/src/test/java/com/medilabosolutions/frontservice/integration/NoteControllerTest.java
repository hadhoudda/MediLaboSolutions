package com.medilabosolutions.frontservice.integration;

import com.medilabosolutions.frontservice.Beans.NoteBean;
import com.medilabosolutions.frontservice.client.NoteGatewayClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteGatewayClient noteGatewayClient;

    private NoteBean notePatient;

    /**
     * Sets up a sample note before each test.
     */
    @BeforeEach
    void setup() {
        // GIVEN: a sample note for patient ID 1
        notePatient = new NoteBean();
        notePatient.setId("note1");
        notePatient.setPatId(1);
        notePatient.setNote("Patient est en bon sante");
    }

    /**
     * Test GET /patient/{patId}/notes returns the note list view with correct model.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testGetNotesByPatient_returnsNoteListView() throws Exception {
        // GIVEN: the note service returns a list with the sample note
        when(noteGatewayClient.getNotesByPatientId(1)).thenReturn(List.of(notePatient));

        // WHEN: a GET request is performed
        // THEN: status is OK, view is "note-patient", model contains the notes
        mockMvc.perform(get("/patient/1/notes"))
                .andExpect(status().isOk())
                .andExpect(view().name("note-patient"))
                .andExpect(model().attributeExists("notes"))
                .andExpect(model().attribute("notes", hasSize(1)));
    }

    /**
     * Test GET /patient/{patId}/notes/add returns the note creation form view.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testShowCreateNoteFormLastPatient_returnsNoteCreateView() throws Exception {
        // GIVEN: a session attribute lastPatientId is set
        // WHEN: a GET request is performed to /patient/1/notes/add
        // THEN: status is OK, view is "note-create", model contains note and patId
        mockMvc.perform(get("/patient/1/notes/add")
                        .sessionAttr("lastPatientId", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("note-create"))
                .andExpect(model().attributeExists("note"))
                .andExpect(model().attribute("patId", 1));
    }

    /**
     * Test POST /patient/{patId}/notes/add redirects to note list after saving a new note.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testSaveNote_redirectsToNoteList() throws Exception {
        // GIVEN: form submission with note text

        // WHEN: POST request to add a note
        // THEN: redirect to note list and createNote is invoked
        mockMvc.perform(post("/patient/1/notes/add")
                        .param("note", "Patient is stable"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patient/1/notes"));

        Mockito.verify(noteGatewayClient).createNote(Mockito.any(NoteBean.class));
    }

    /**
     * Test GET /patient/{patId}/notes/edit/{id} returns the note edit form view.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testShowEditNoteForm_returnsNoteEditView() throws Exception {
        // GIVEN: the note service returns the sample note
        when(noteGatewayClient.getNotesByPatientId(1)).thenReturn(List.of(notePatient));

        // WHEN: GET request is performed to edit a note
        // THEN: status is OK, view is "note-edit", model contains note and patId
        mockMvc.perform(get("/patient/1/notes/edit/note1"))
                .andExpect(status().isOk())
                .andExpect(view().name("note-edit"))
                .andExpect(model().attributeExists("note"))
                .andExpect(model().attribute("patId", 1));
    }

    /**
     * Test POST /patient/{patId}/notes/edit/{id} redirects to note list after updating a note.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testEditNote_redirectsToNoteList() throws Exception {
        // GIVEN: form submission with updated note text

        // WHEN: POST request to edit the note
        // THEN: redirect to note list and updateNote is invoked
        mockMvc.perform(post("/patient/1/notes/edit/note1")
                        .param("note", "Updated note"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patient/1/notes"));

        Mockito.verify(noteGatewayClient).updateNote(Mockito.eq("note1"), Mockito.any(NoteBean.class));
    }

    /**
     * Test POST /patient/{patId}/notes/{id} redirects to note list after deleting a note.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testDeleteNote_redirectsToNoteList() throws Exception {
        // GIVEN: a note exists with ID "note1"

        // WHEN: POST request to delete the note
        // THEN: redirect to note list and deleteNotePatientById is invoked
        mockMvc.perform(post("/patient/1/notes/note1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patient/1/notes"));

        Mockito.verify(noteGatewayClient).deleteNotePatientById("note1");
    }
}
