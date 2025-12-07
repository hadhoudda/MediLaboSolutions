package com.medilabosolutions.frontservice.controller;

import com.medilabosolutions.frontservice.Beans.NoteBean;

import com.medilabosolutions.frontservice.client.NoteGatewayClient;
import feign.FeignException;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping
public class NoteController {

    private final NoteGatewayClient noteGatewayClient;

    public NoteController(NoteGatewayClient noteGatewayClient) {
        this.noteGatewayClient = noteGatewayClient;
    }

    // Liste des notes d'un patient (protégée)
    @GetMapping("/patient/{patId}/notes")
    public String getNotesByPatient(@PathVariable int patId, Model model, HttpSession session) {

        // Stocke le patient ID en session
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

    @GetMapping("/patient/{patId}/notes/add")
    public String showCreateNoteFormLastPatient(Model model, HttpSession session) {

        // Stocke le patient ID en session
        Integer patId = (Integer) session.getAttribute("lastPatientId");
        model.addAttribute("patId", patId);
        model.addAttribute("note", new NoteBean());
        return "note-create";
    }

    @PostMapping("/patient/{patId}/notes/add")
    public String saveNote(@PathVariable int patId, @ModelAttribute("note") NoteBean noteBean) {
        noteBean.setPatId(patId);
        // sauvegarde le note
        NoteBean note = noteGatewayClient.createNote(noteBean);
        return "redirect:/patient/" + patId + "/notes";
    }

    // Affiche le formulaire pour éditer une note existante
    @GetMapping("/patient/{patId}/notes/edit/{id}")
    public String showEditNoteForm(@PathVariable int patId,
                                   @PathVariable String id,
                                   Model model) {

        // Récupération de la list de note
        List<NoteBean> notes = noteGatewayClient.getNotesByPatientId(patId);
        // Filtre la note existante
        NoteBean note = notes.stream()
                .filter(n -> n.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Note introuvable"));

        model.addAttribute("patId", patId);
        model.addAttribute("note", note);

        return "note-edit";
    }

    @PostMapping("/patient/{patId}/notes/edit/{id}")
    public String editNote(@PathVariable int patId, @PathVariable String id,
                           @ModelAttribute("note") NoteBean noteBean) {
        noteBean.setPatId(patId); // s'assure que la note reste attachée au patient
        noteGatewayClient.updateNote(id, noteBean);
        return "redirect:/patient/" + patId + "/notes";
    }

    @PostMapping("/patient/{patId}/notes/{id}")
    public String deleteNote(@PathVariable int patId, @PathVariable String id) {
        noteGatewayClient.deleteNotePatientById(id);
        return "redirect:/patient/" + patId + "/notes";
    }

}
