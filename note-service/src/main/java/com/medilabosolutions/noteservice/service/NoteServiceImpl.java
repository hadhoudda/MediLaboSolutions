package com.medilabosolutions.noteservice.service;

import com.medilabosolutions.noteservice.model.Note;
import com.medilabosolutions.noteservice.repository.NoteRepository;
import com.medilabosolutions.noteservice.service.contracts.INoteService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class NoteServiceImpl implements INoteService {

    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }


    public List<Note> findAllNoteByPatId(Integer patId) {
        return noteRepository.findByPatIdOrderByCreatedNoteDateDesc(patId);
    }

    @Override
    public Note addNote(Note note) {
        Assert.isTrue(note.getId() == null, "Le note ne doit pas avoir d'ID lors de la création");
        return noteRepository.save(note);
    }

    @Override
    public Note updateNote(Note note) {
        Assert.isTrue(note.getId() == null, "L'ID est requis pour la mise à jour");
        return noteRepository.save(note);
    }

    @Override
    public void deleteNote(String id) {
        noteRepository.deleteById(id);
    }

    @Override
    public void deleteAllNoteByPatId(String id) {
        noteRepository.deleteById(id);
    }
}
