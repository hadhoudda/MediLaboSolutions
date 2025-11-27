package com.medilabosolutions.noteservice.repository;

import com.medilabosolutions.noteservice.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

    List<Note> findByPatIdOrderByCreatedNoteDateDesc(int patientId);


}
