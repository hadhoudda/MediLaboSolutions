package com.medilabosolutions.noteservice.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabosolutions.noteservice.model.Note;
import com.medilabosolutions.noteservice.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

/**
 * DataLoader class responsible for initializing the database with sample notes data.
 *
 * <p>This component runs on application startup (excluding the 'test' profile) and
 * optionally recreates the MongoDB collection if the `application.database.recreate`
 * property is set to true.</p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test")
public class DataLoader implements CommandLineRunner {

    @Value("${application.database.recreate}")
    private boolean recreateDb; // Flag to determine whether to recreate the database

    private final NoteRepository noteRepository;
    private final ObjectMapper objectMapper;

    /**
     * Executes on application startup.
     *
     * <p>If the `recreateDb` flag is true, deletes all existing notes,
     * reads sample data from `/data/notes.json`, and saves it to MongoDB.</p>
     *
     * @param args command-line arguments (ignored)
     * @throws Exception if reading or saving notes fails
     */
    @Override
    public void run(String... args) throws Exception {

        if (recreateDb) {
            log.warn(" Suppression de toutes les notes !"); // Warn that existing data will be deleted

            noteRepository.deleteAll(); // Delete all existing notes

            // Load initial notes data from JSON file in resources
            InputStream inputStream = getClass().getResourceAsStream("/data/notes.json");

            List<Note> notes = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<Note>>() {}
            );

            noteRepository.saveAll(notes); // Save all loaded notes to MongoDB
            log.info("Données initiales MongoDB importées !");
        }

    }
}
