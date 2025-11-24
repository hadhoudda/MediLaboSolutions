package com.medilabosolutions.patientservice.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabosolutions.patientservice.dto.PatientDto;
import com.medilabosolutions.patientservice.model.Patient;
import com.medilabosolutions.patientservice.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

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

    @BeforeEach
    void setup() {
        patientRepository.deleteAll(); // Nettoyer la base avant chaque test
    }

    @Test
    void testGetAllPatients_emptyList() throws Exception {
        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllPatients_nonEmptyList() throws Exception {
        Patient patient = Patient.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(
                        Date.from(
                                LocalDate.of(1990, 1, 1)
                                        .atStartOfDay(ZoneId.systemDefault())
                                        .toInstant()
                        )
                )
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

    @Test
    void testGetPatientById_found() throws Exception {
        Patient patient = Patient.builder()
                .firstName("Jane")
                .lastName("Doe")
                .dateOfBirth(
                        Date.from(
                                LocalDate.of(1995, 5, 5)
                                        .atStartOfDay(ZoneId.systemDefault())
                                        .toInstant()
                        )
                )
                .gender("F")
                .build();
        Patient saved = patientRepository.save(patient);

        mockMvc.perform(get("/api/patients/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Jane")))
                .andExpect(jsonPath("$.gender", is("F")));
    }

    @Test
    void testCreatePatient_success() throws Exception {
        PatientDto dto = PatientDto.builder()
                .firstName("Alice")
                .lastName("Smith")
                .dateOfBirth(
                        Date.from(
                                LocalDate.of(2000, 1, 1)
                                        .atStartOfDay(ZoneId.systemDefault())
                                        .toInstant()
                        )
                )
                .gender("F")
                .address("456 Street")
                .telephoneNumber("555-1234")
                .build();

        mockMvc.perform(post("/api/patients")
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
                .dateOfBirth(
                        Date.from(
                                LocalDate.of(2030, 1, 1)
                                        .atStartOfDay(ZoneId.systemDefault())
                                        .toInstant()
                        )
                )
                .gender("X") // invalid
                .build();

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)));
    }

    @Test
    void testUpdatePatient_success() throws Exception {
        Patient patient = Patient.builder()
                .firstName("Bob")
                .lastName("Marley")
                .dateOfBirth(
                        Date.from(
                                LocalDate.of(1980, 2, 2)
                                        .atStartOfDay(ZoneId.systemDefault())
                                        .toInstant()
                        )
                )
                .gender("M")
                .build();
        Patient saved = patientRepository.save(patient);

        PatientDto dto = PatientDto.builder()
                .firstName("Bobby")
                .lastName("Marley")
                .dateOfBirth(
                        Date.from(
                                LocalDate.of(1980, 2, 2)
                                        .atStartOfDay(ZoneId.systemDefault())
                                        .toInstant()
                        )
                )
                .gender("M")
                .build();

        mockMvc.perform(put("/api/patients/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Bobby")));
    }
}
