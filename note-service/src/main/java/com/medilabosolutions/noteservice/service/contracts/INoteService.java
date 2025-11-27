package com.medilabosolutions.noteservice.service.contracts;

import com.medilabosolutions.noteservice.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface INoteService {

    List<Note> findAllNoteByPatId(Integer patId);

    Note addNote(Note note);

    Note updateNote(Note note);

    void deleteNote(String id);

    void deleteAllNoteByPatId(String id);
}
