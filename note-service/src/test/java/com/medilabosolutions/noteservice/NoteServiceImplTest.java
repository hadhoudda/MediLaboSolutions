package com.medilabosolutions.noteservice;



import com.medilabosolutions.noteservice.exceptions.NoteNotFoundException;
import com.medilabosolutions.noteservice.model.Note;
import com.medilabosolutions.noteservice.repository.NoteRepository;
import com.medilabosolutions.noteservice.service.NoteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NoteServiceImplTest {

    private NoteRepository noteRepository;
    private NoteServiceImpl noteService;

    @BeforeEach
    void setUp() {
        noteRepository = mock(NoteRepository.class);
        noteService = new NoteServiceImpl(noteRepository);
    }

    @Test
    void testFindAllNoteByPatId() {
        Note n1 = new Note();
        Note n2 = new Note();
        List<Note> notes = Arrays.asList(n1, n2);

        when(noteRepository.findByPatIdOrderByUpdatedNoteDateDesc(5))
                .thenReturn(notes);

        List<Note> result = noteService.findAllNoteByPatId(5);

        assertEquals(2, result.size());
        verify(noteRepository).findByPatIdOrderByUpdatedNoteDateDesc(5);
    }

    @Test
    void testFindNoteById() {
        Note note = new Note();
        note.setId("abc");

        when(noteRepository.findById("abc"))
                .thenReturn(Optional.of(note));

        Optional<Note> found = noteService.findNoteById("abc");

        assertTrue(found.isPresent());
        assertEquals("abc", found.get().getId());
    }

    @Test
    void testAddNote() {
        Note note = new Note();
        note.setNote("test note");

        when(noteRepository.save(note)).thenReturn(note);

        Note saved = noteService.addNote(note);

        assertNotNull(saved);
        verify(noteRepository).save(note);
    }

    @Test
    void testUpdateNote() throws Exception, NoteNotFoundException {
        Note existing = new Note();
        existing.setId("123");
        existing.setNote("old");
        existing.setUpdatedNoteDate(Instant.now());

        when(noteRepository.findById("123")).thenReturn(Optional.of(existing));
        when(noteRepository.save(any(Note.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Note updated = noteService.updateNote("123", "new note");

        assertEquals("new note", updated.getNote());
        assertNotNull(updated.getUpdatedNoteDate());
        verify(noteRepository).findById("123");
        verify(noteRepository).save(existing);
    }

    @Test
    void testUpdateNote_NotFound() {
        when(noteRepository.findById("404")).thenReturn(Optional.empty());

        assertThrows(NoteNotFoundException.class,
                () -> noteService.updateNote("404", "xxx"));
    }

    @Test
    void testDeleteNote() throws Exception, NoteNotFoundException {
        Note existing = new Note();
        existing.setId("999");

        when(noteRepository.findById("999")).thenReturn(Optional.of(existing));

        noteService.deleteNote("999");

        verify(noteRepository).delete(existing);
    }

    @Test
    void testDeleteNote_NotFound() {
        when(noteRepository.findById("not-found")).thenReturn(Optional.empty());

        assertThrows(NoteNotFoundException.class,
                () -> noteService.deleteNote("not-found"));
    }
}
