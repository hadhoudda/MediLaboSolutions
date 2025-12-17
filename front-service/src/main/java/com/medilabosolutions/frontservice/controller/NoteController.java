package com.medilabosolutions.frontservice.controller;

import com.medilabosolutions.frontservice.Beans.NoteBean;
import com.medilabosolutions.frontservice.client.NoteGatewayClient;
import feign.FeignException;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsible for managing patient notes.
 *
 * <p>Provides endpoints to list, create, edit, and delete notes
 * for a specific patient. Integrates with the Note microservice via
 * {@link NoteGatewayClient}.</p>
 */
@Controller
@RequestMapping
public class NoteController {

    private final NoteGatewayClient noteGatewayClient;

    public NoteController(NoteGatewayClient noteGatewayClient) {
        this.noteGatewayClient = noteGatewayClient;
    }

    /**
     * Displays the list of notes for a specific patient.
     * Stores the patient ID in the HTTP session.
     *
     * @param patId   the patient ID
     * @param model   the Spring model to pass data to the view
     * @param session the HTTP session to store lastPatientId
     * @return the view name "note-patient"
     */
    @GetMapping("/patient/{patId}/notes")
    public String getNotesByPatient(@PathVariable int patId, Model model, HttpSession session) {

        // Store the patient ID in session
        session.setAttribute("lastPatientId", patId);

        try {
            List<NoteBean> notes = noteGatewayClient.getNotesByPatientId(patId);
            model.addAttribute("notes", notes);

            if (notes.isEmpty()) {
                model.addAttribute("message", "Aucune note trouvée pour ce patient.");
            }

        } catch (FeignException.NotFound e) {
            model.addAttribute("message", "Aucune note trouvée pour ce patient.");
            model.addAttribute("notes", List.of());
        }
        return "note-patient";
    }

    /**
     * Shows the form to create a new note for the last patient stored in session.
     *
     * @param model   the Spring model to pass data to the view
     * @param session the HTTP session to retrieve lastPatientId
     * @return the view name "note-create"
     */
    @GetMapping("/patient/{patId}/notes/add")
    public String showCreateNoteFormLastPatient(Model model, HttpSession session) {

        Integer patId = (Integer) session.getAttribute("lastPatientId");
        model.addAttribute("patId", patId);
        model.addAttribute("note", new NoteBean());
        return "note-create";
    }

    /**
     * Saves a newly created note for a specific patient.
     *
     * @param patId    the patient ID
     * @param noteBean the note data from the form
     * @return redirect to the patient notes list
     */
    @PostMapping("/patient/{patId}/notes/add")
    public String saveNote(@PathVariable int patId, @ModelAttribute("note") NoteBean noteBean) {
        noteBean.setPatId(patId);
        noteGatewayClient.createNote(noteBean);
        return "redirect:/patient/" + patId + "/notes";
    }

    /**
     * Shows the form to edit an existing note.
     *
     * @param patId the patient ID
     * @param id    the note ID
     * @param model the Spring model to pass data to the view
     * @return the view name "note-edit"
     */
    @GetMapping("/patient/{patId}/notes/edit/{id}")
    public String showEditNoteForm(@PathVariable int patId,
                                   @PathVariable String id,
                                   Model model) {

        List<NoteBean> notes = noteGatewayClient.getNotesByPatientId(patId);

        // Find the note by ID or throw exception if not found
        NoteBean note = notes.stream()
                .filter(n -> n.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Note introuvable"));

        model.addAttribute("patId", patId);
        model.addAttribute("note", note);

        return "note-edit";
    }

    /**
     * Updates an existing note for a patient.
     *
     * @param patId    the patient ID
     * @param id       the note ID
     * @param noteBean the updated note data
     * @return redirect to the patient notes list
     */
    @PostMapping("/patient/{patId}/notes/edit/{id}")
    public String editNote(@PathVariable int patId, @PathVariable String id,
                           @ModelAttribute("note") NoteBean noteBean) {
        noteBean.setPatId(patId); // Ensure the note remains attached to the patient
        noteGatewayClient.updateNote(id, noteBean);
        return "redirect:/patient/" + patId + "/notes";
    }

    /**
     * Deletes a note for a specific patient.
     *
     * @param patId the patient ID
     * @param id    the note ID
     * @return redirect to the patient notes list
     */
    @PostMapping("/patient/{patId}/notes/{id}")
    public String deleteNote(@PathVariable int patId, @PathVariable String id) {
        noteGatewayClient.deleteNotePatientById(id);
        return "redirect:/patient/" + patId + "/notes";
    }

}
