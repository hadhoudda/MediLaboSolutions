package com.medilabosolutions.patientservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabosolutions.patientservice.dto.PatientDto;
import com.medilabosolutions.patientservice.model.Patient;
import com.medilabosolutions.patientservice.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link com.medilabosolutions.patientservice.controller.PatientController}.
 *
 * Uses MockMvc to simulate HTTP requests and verify the API behavior.
 * Tests include retrieving all patients, fetching by ID, creating, updating, and validation errors.
 */
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class PatientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Clean the database before each test to ensure isolated test scenarios.
     */
    @BeforeEach
    void setup() {
        patientRepository.deleteAll();
    }

    /**
     * Test that GET /api/patients returns 204 No Content when the database is empty.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testGetAllPatients_emptyList() throws Exception {
        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isNoContent());
    }

    /**
     * Test that GET /api/patients returns a list of patients when database has records.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testGetAllPatients_nonEmptyList() throws Exception {
        Patient patient = Patient.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .gender("M")
                .address("123 Street")
                .telephoneNumber("123456")
                .build();
        patientRepository.save(patient);

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("John")));
    }

    /**
     * Test that GET /api/patients/{id} returns a specific patient if it exists.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testGetPatientById_found() throws Exception {
        Patient patient = Patient.builder()
                .firstName("Jane")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1995, 5, 5))
                .gender("F")
                .build();
        Patient saved = patientRepository.save(patient);

        mockMvc.perform(get("/api/patients/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Jane")))
                .andExpect(jsonPath("$.gender", is("F")));
    }

    /**
     * Test that POST /api/patients successfully creates a new patient.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testCreatePatient_success() throws Exception {
        PatientDto patientDto = PatientDto.builder()
                .firstName("Alice")
                .lastName("Smith")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .gender("F")
                .address("456 Street")
                .telephoneNumber("555-1234")
                .build();

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("Alice")))
                .andExpect(jsonPath("$.gender", is("F")));
    }

    /**
     * Test that POST /api/patients returns 400 Bad Request when validation fails.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testCreatePatient_validationFail() throws Exception {
        PatientDto patientDto = PatientDto.builder()
                .firstName("") // invalid
                .lastName("")  // invalid
                .dateOfBirth(LocalDate.of(2030, 1, 1)) // invalid future date
                .gender("X")  // invalid gender
                .build();

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)));
    }

    /**
     * Test that PUT /api/patients/{id} successfully updates an existing patient.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testUpdatePatient_success() throws Exception {
        Patient patient = Patient.builder()
                .firstName("Bob")
                .lastName("Marley")
                .dateOfBirth(LocalDate.of(1980, 2, 2))
                .gender("M")
                .build();
        Patient saved = patientRepository.save(patient);

        PatientDto patientDto = PatientDto.builder()
                .firstName("Bobby")
                .lastName("Marley")
                .dateOfBirth(LocalDate.of(1980, 2, 2))
                .gender("M")
                .build();

        mockMvc.perform(put("/api/patients/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Bobby")));
    }
}
