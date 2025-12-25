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

/**
 * REST controller for managing Notes.
 */
@Slf4j
@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final INoteService iNoteService;
    private final NoteMapper noteMapper;

    /**
     * Retrieves all notes for a specific patient.
     *
     * @param patId   Patient ID
     * @param request HttpServletRequest for error path
     * @return List of notes or error response if none found
     */
    @GetMapping("/patient/{patId}")
    public ResponseEntity<?> getAllNoteByPatientId(@PathVariable int patId,
                                                   HttpServletRequest request) {

        List<Note> noteList = iNoteService.findAllNoteByPatId(patId);

        if (noteList.isEmpty()) {
            log.info("No notes found for patient {}", patId);

            ApiErrorResponse error = ApiErrorResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("No notes found for patient with id: " + patId + " or patient does not exist.")
                    .timestamp(LocalDateTime.now())
                    .path(request.getRequestURI())
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        return ResponseEntity.ok(noteMapper.toDtoList(noteList));
    }

    /**
     * Creates a new note for a patient.
     *
     * @param noteDto Note DTO to create
     * @param request HttpServletRequest for error path
     * @return Created note or error response
     */
    @PostMapping("/patient")
    public ResponseEntity<?> createNote(@Valid @RequestBody NoteDto noteDto,
                                        HttpServletRequest request) {
        try {
            Note note = iNoteService.addNote(noteMapper.toEntity(noteDto));
            log.info("Note successfully added for patient {}", note.getPatId());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(noteMapper.toDto(note));

        } catch (Exception e) {
            log.error("Error adding note: {}", e.getMessage());

            ApiErrorResponse error = ApiErrorResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Unable to create note: " + e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .path(request.getRequestURI())
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Updates a note for a patient.
     *
     * @param noteUpdateDto Note update DTO
     * @param id            Note ID
     * @param request       HttpServletRequest for error path
     * @return Updated note or error response
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateNotePatient(
            @Valid @RequestBody NoteUpdateDto noteUpdateDto,
            @PathVariable String id,
            HttpServletRequest request) {

        try {
            Note existing = iNoteService.findNoteById(id)
                    .orElseThrow(() -> new NoteNotFoundException("Note with id " + id + " does not exist"));

            // Update note content and updated date
            existing.setNote(noteUpdateDto.getNote());
            existing.setUpdatedNoteDate(Instant.now());

            // Save updated note
            Note updated = iNoteService.updateNote(id, noteUpdateDto.getNote());

            log.info("Note successfully updated for patient {}", updated.getPatId());

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
                    .message("Unable to update note: " + e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .path(request.getRequestURI())
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Deletes a note by its ID.
     *
     * @param id      Note ID
     * @param request HttpServletRequest for error path
     * @return Success message or error response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotePatient(@PathVariable String id,
                                               HttpServletRequest request) {
        try {
            iNoteService.deleteNote(id);
            log.info("Note with id {} successfully deleted", id);

            return ResponseEntity.ok("Note deleted");

        } catch (NoteNotFoundException e) {
            log.error("Error deleting note: {}", e.getMessage());

            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .path(request.getRequestURI())
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

        } catch (Exception e) {
            log.error("Server error when deleting note: {}", e.getMessage());

            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Unable to delete note: " + e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .path(request.getRequestURI())
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
