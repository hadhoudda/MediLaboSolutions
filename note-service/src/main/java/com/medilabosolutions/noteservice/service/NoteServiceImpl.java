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

    /**
     * Constructor injection of NoteRepository.
     *
     * @param noteRepository repository for Note entities
     */
    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    /**
     * Finds all notes for a given patient, ordered by updated date descending.
     *
     * @param patId patient ID
     * @return list of notes for the patient
     */
    public List<Note> findAllNoteByPatId(Integer patId) {
        return noteRepository.findByPatIdOrderByUpdatedNoteDateDesc(patId);
    }

    /**
     * Finds a note by its ID.
     *
     * @param id note ID
     * @return Optional containing the note if found
     */
    @Override
    public Optional<Note> findNoteById(String id) {
        return noteRepository.findById(id);
    }

    /**
     * Adds a new note to the database.
     *
     * @param note note entity to create (ID must be null)
     * @return created note
     */
    @Override
    public Note addNote(Note note) {
        Assert.isTrue(note.getId() == null, "The note must not have an ID when creating");
        return noteRepository.save(note);
    }

    /**
     * Updates an existing note's content and updated timestamp.
     *
     * @param id      note ID
     * @param newNote new note content
     * @return updated note
     * @throws NoteNotFoundException if the note does not exist
     */
    @Override
    public Note updateNote(String id, String newNote) throws NoteNotFoundException {
        Note existing = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("Note with id " + id + " does not exist"));

        existing.setNote(newNote);
        existing.setUpdatedNoteDate(Instant.now());

        return noteRepository.save(existing);
    }

    /**
     * Deletes a note by its ID.
     *
     * @param id note ID
     * @throws NoteNotFoundException if the note does not exist
     */
    @Override
    public void deleteNote(String id) throws NoteNotFoundException {
        Note existing = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("Note with id " + id + " does not exist"));

        noteRepository.delete(existing);
        log.info("Note successfully deleted (id={})", id);
    }
}
