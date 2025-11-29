package com.medilabosolutions.noteservice.controller;

import com.medilabosolutions.noteservice.dto.ApiErrorResponse;
import com.medilabosolutions.noteservice.dto.NoteDto;
import com.medilabosolutions.noteservice.dto.NoteUpdateDto;
import com.medilabosolutions.noteservice.exceptions.NoteNotFoundException;
import com.medilabosolutions.noteservice.mapper.NoteMapper;
import com.medilabosolutions.noteservice.model.Note;
import com.medilabosolutions.noteservice.service.contracts.INoteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final INoteService iNoteService;
    private final NoteMapper noteMapper;

    // les notes pour un patient
    @GetMapping("/patient/{patId}")
    public ResponseEntity<?> getAllNoteByPatientId(@PathVariable int patId,
                                                   HttpServletRequest request) {

        List<Note> noteList = iNoteService.findAllNoteByPatId(patId);

        if (noteList.isEmpty()) {
            log.info("Aucune note trouvée pour le patient {}", patId);

            ApiErrorResponse error = ApiErrorResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Aucune note trouvée pour le patient avec l'id : " + patId + " ou le patient n'existe pas.")
                    .timestamp(LocalDateTime.now())
                    .path(request.getRequestURI())
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        return ResponseEntity.ok(noteMapper.toDtoList(noteList));
    }

    //ajout note pour un patient
    @PostMapping("/patient")
    public ResponseEntity<?> createNote(@Valid @RequestBody NoteDto noteDto,
                                        HttpServletRequest request) {
        try {
            Note note = iNoteService.addNote(noteMapper.toEntity(noteDto));
            log.info("Note ajoutée avec succès pour le patient {}", note.getPatId());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(noteMapper.toDto(note));

        } catch (Exception e) {
            // En cas d’erreur côté serveur
            log.error("Erreur lors de l'ajout de la note : {}", e.getMessage());

            ApiErrorResponse error = ApiErrorResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Impossible de créer la note : " + e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .path(request.getRequestURI())
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Modifier uniquement la note d’un patient
    @PutMapping("/{id}")
    public ResponseEntity<?> updateNotePatient(
            @Valid @RequestBody NoteUpdateDto noteUpdateDto,
            @PathVariable String id,
            HttpServletRequest request) {

        try {
            Note existing = iNoteService.findNoteById(id)
                    .orElseThrow(() -> new NoteNotFoundException("La note avec l'id " + id + " n'existe pas"));

            // Mettre à jour uniquement le note et la date de mis à jour
            existing.setNote(noteUpdateDto.getNote());
            existing.setUpdatedNoteDate(Instant.now());
            // Sauvegarde
            Note updated = iNoteService.updateNote(id, noteUpdateDto.getNote());

            log.info("Note modifiée avec succès pour le patient {}", updated.getPatId());

            return ResponseEntity.ok(noteMapper.toDto(updated));

        } catch (NoteNotFoundException e) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .path(request.getRequestURI())
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

        } catch (Exception e) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Impossible de modifier la note : " + e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .path(request.getRequestURI())
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    //supprime une note
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotePatient(@PathVariable String id,
                                               HttpServletRequest request) {
        try {
            iNoteService.deleteNote(id);
            log.info("Note avec id {} supprimée avec succès", id);

            return ResponseEntity.ok("Note supprimée");

        } catch (NoteNotFoundException e) {
            log.error("Erreur suppression note : {}", e.getMessage());

            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .path(request.getRequestURI())
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

        } catch (Exception e) {
            log.error("Erreur serveur lors de la suppression de la note : {}", e.getMessage());

            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Impossible de supprimer la note : " + e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .path(request.getRequestURI())
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }



}
