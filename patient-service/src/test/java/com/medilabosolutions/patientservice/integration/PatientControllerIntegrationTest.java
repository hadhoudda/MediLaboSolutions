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

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testGetAllPatients_emptyList() throws Exception {
        // Given: the database is empty

        // When: a GET request is made to /api/patients
        mockMvc.perform(get("/api/patients"))
                // Then: the response should be 204 No Content
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testGetAllPatients_nonEmptyList() throws Exception {
        // Given: a patient exists in the database
        Patient patient = Patient.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .gender("M")
                .address("123 Street")
                .telephoneNumber("123456")
                .build();
        patientRepository.save(patient);

        // When: a GET request is made to /api/patients
        mockMvc.perform(get("/api/patients"))
                // Then: the response should be 200 OK and contain the patient
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("John")));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testGetPatientById_found() throws Exception {
        // Given: a patient is saved in the database
        Patient patient = Patient.builder()
                .firstName("Jane")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1995, 5, 5))
                .gender("F")
                .build();
        Patient saved = patientRepository.save(patient);

        // When: a GET request is made to /api/patients/{id}
        mockMvc.perform(get("/api/patients/{id}", saved.getId()))
                // Then: the response should be 200 OK and contain the correct patient
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Jane")))
                .andExpect(jsonPath("$.gender", is("F")));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testCreatePatient_success() throws Exception {
        // Given: a valid PatientDto
        PatientDto patientDto = PatientDto.builder()
                .firstName("Alice")
                .lastName("Smith")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .gender("F")
                .address("456 Street")
                .telephoneNumber("555-1234")
                .build();

        // When: a POST request is made to /api/patients
        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDto)))
                // Then: the response should be 201 Created and contain the created patient
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("Alice")))
                .andExpect(jsonPath("$.gender", is("F")));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testCreatePatient_validationFail() throws Exception {
        // Given: an invalid PatientDto
        PatientDto patientDto = PatientDto.builder()
                .firstName("") // invalid
                .lastName("")  // invalid
                .dateOfBirth(LocalDate.of(2050, 1, 1)) // invalid future date
                .gender("X")  // invalid gender
                .build();

        // When: a POST request is made to /api/patients
        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDto)))
                // Then: the response should be 400 Bad Request
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testUpdatePatient_success() throws Exception {
        // Given: an existing patient
        Patient patient = Patient.builder()
                .firstName("Bob")
                .lastName("Marley")
                .dateOfBirth(LocalDate.of(1980, 2, 2))
                .gender("M")
                .build();
        Patient saved = patientRepository.save(patient);

        // And: a PatientDto with updated data
        PatientDto patientDto = PatientDto.builder()
                .firstName("Bobby")
                .lastName("Marley")
                .dateOfBirth(LocalDate.of(1980, 2, 2))
                .gender("M")
                .build();

        // When: a PUT request is made to /api/patients/{id}
        mockMvc.perform(put("/api/patients/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDto)))
                // Then: the response should be 200 OK and the patient should be updated
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Bobby")));
    }
}
