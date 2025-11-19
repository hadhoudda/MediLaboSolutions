package com.medilabosolutions.back_patient.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabosolutions.back_patient.config.TestSecurityConfig;
import com.medilabosolutions.back_patient.dto.PatientDto;
import com.medilabosolutions.back_patient.model.Patient;
import com.medilabosolutions.back_patient.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc
class PatientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        patientRepository.deleteAll(); // Nettoyer la base avant chaque test
    }

    @Test
    void testGetAllPatients_emptyList() throws Exception {
        mockMvc.perform(get("/patients"))
                .andExpect(status().isNoContent());
    }

    @Test
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

        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("John")));
    }

    @Test
    void testGetPatientById_found() throws Exception {
        Patient patient = Patient.builder()
                .firstName("Jane")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1995, 5, 5))
                .gender("F")
                .build();
        Patient saved = patientRepository.save(patient);

        mockMvc.perform(get("/patients/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Jane")))
                .andExpect(jsonPath("$.gender", is("F")));
    }

    @Test
    void testCreatePatient_success() throws Exception {
        PatientDto dto = PatientDto.builder()
                .firstName("Alice")
                .lastName("Smith")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .gender("F")
                .address("456 Street")
                .telephoneNumber("555-1234")
                .build();

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("Alice")))
                .andExpect(jsonPath("$.gender", is("F")));
    }

    @Test
    void testCreatePatient_validationFail() throws Exception {
        PatientDto dto = PatientDto.builder()
                .firstName("") // invalid
                .lastName("")
                .dateOfBirth(LocalDate.of(2025, 1, 1)) // future date invalid
                .gender("X") // invalid
                .build();

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdatePatient_success() throws Exception {
        Patient patient = Patient.builder()
                .firstName("Bob")
                .lastName("Marley")
                .dateOfBirth(LocalDate.of(1980, 2, 2))
                .gender("M")
                .build();
        Patient saved = patientRepository.save(patient);

        PatientDto dto = PatientDto.builder()
                .firstName("Bobby")
                .lastName("Marley")
                .dateOfBirth(LocalDate.of(1980, 2, 2))
                .gender("M")
                .build();

        mockMvc.perform(put("/patients/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Bobby")));
    }
}
