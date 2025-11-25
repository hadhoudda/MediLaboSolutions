package com.medilabosolutions.noteservice.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabosolutions.noteservice.model.Note;
import com.medilabosolutions.noteservice.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    @Value("${application.database.recreate}")
    private boolean recreateDb;

    private final NoteRepository noteRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {

        if (recreateDb){
            log.warn(" Suppression de toutes les notes !");
            noteRepository.deleteAll();
            InputStream inputStream = getClass().getResourceAsStream("/data/notes.json");

            List<Note> notes = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<Note>>() {}
            );

            noteRepository.saveAll(notes);
            log.info("Données initiales MongoDB importées !");
        }

    }
}