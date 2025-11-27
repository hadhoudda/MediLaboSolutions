package com.medilabosolutions.noteservice.controller;

import com.medilabosolutions.noteservice.dto.ApiErrorResponse;
import com.medilabosolutions.noteservice.dto.NoteDto;
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

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final INoteService iNoteService;
    private final NoteMapper noteMapper;

    //tous les notes pour un patient
    @GetMapping("/{patId}")
    public ResponseEntity<?> getAllNoteByPatientId(@PathVariable int patId,
                                                   HttpServletRequest request
    ) {

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
    @PostMapping
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

    //modifie note pour un patient



}
