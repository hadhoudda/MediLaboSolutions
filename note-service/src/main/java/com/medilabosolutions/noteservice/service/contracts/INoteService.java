package com.medilabosolutions.noteservice.service.contracts;

import com.medilabosolutions.noteservice.exceptions.NoteNotFoundException;
import com.medilabosolutions.noteservice.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface INoteService {

    List<Note> findAllNoteByPatId(Integer patId);

    Optional<Note> findNoteById(String id);

    Note addNote(Note note);

    Note updateNote(String id, String newNote) throws NoteNotFoundException;

    void deleteNote(String id) throws NoteNotFoundException;

}
