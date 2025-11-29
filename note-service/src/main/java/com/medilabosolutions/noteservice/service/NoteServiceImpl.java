package com.medilabosolutions.noteservice.service;

import com.medilabosolutions.noteservice.exceptions.NoteNotFoundException;
import com.medilabosolutions.noteservice.model.Note;
import com.medilabosolutions.noteservice.repository.NoteRepository;
import com.medilabosolutions.noteservice.service.contracts.INoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class NoteServiceImpl implements INoteService {

    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }


    public List<Note> findAllNoteByPatId(Integer patId) {
        return noteRepository.findByPatIdOrderByUpdatedNoteDateDesc(patId);
    }

    @Override
    public Optional<Note> findNoteById(String id) {
        return noteRepository.findById(id);
    }

    @Override
    public Note addNote(Note note) {
        Assert.isTrue(note.getId() == null, "Le note ne doit pas avoir d'ID lors de la création");
        return noteRepository.save(note);
    }

    @Override
    public Note updateNote(String id, String newNote) throws NoteNotFoundException {
        Note existing = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("La note avec l'id " + id + " n'existe pas"));

        existing.setNote(newNote);
        existing.setUpdatedNoteDate(Instant.now());

        return noteRepository.save(existing);
    }


    @Override
    public void deleteNote(String id) throws NoteNotFoundException {
        Note existing = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("La note avec l'id " + id + " n'existe pas"));

        noteRepository.delete(existing);
        log.info("Note supprimée avec succès (id={})", id);
    }

}
