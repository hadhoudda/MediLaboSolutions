package com.medilabosolutions.patientservice.controller;

import com.medilabosolutions.patientservice.dto.PatientDto;
import com.medilabosolutions.patientservice.exceptions.PatientNotFoundException;
import com.medilabosolutions.patientservice.mapper.PatientMapper;
import com.medilabosolutions.patientservice.model.Patient;
import com.medilabosolutions.patientservice.service.contracts.IPatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing patients.
 *
 * Exposes endpoints to:
 * - Retrieve all patients
 * - Retrieve a patient by ID
 * - Create a new patient
 * - Update an existing patient
 *
 * Uses PatientMapper to convert between entities and DTOs.
 */
@Slf4j
@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final IPatientService patientService;
    private final PatientMapper patientMapper;

    /**
     * GET /api/patients
     *
     * Retrieves all patients.
     * Returns 204 No Content if the list is empty.
     *
     * @return ResponseEntity containing a list of PatientDto
     */
    @GetMapping
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        var patients = patientService.findAllPatients();

        if (patients.isEmpty()) {
            log.info("The patient list is empty.");
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        // Map entity list to DTO list and return 200 OK
        return ResponseEntity.ok(patientMapper.toDtoList(patients));
    }

    /**
     * GET /api/patients/{id}
     *
     * Retrieves a single patient by ID.
     * Throws PatientNotFoundException if the patient does not exist.
     *
     * @param id the patient ID
     * @return ResponseEntity containing the PatientDto
     * @throws PatientNotFoundException if patient not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable int id) throws PatientNotFoundException {
        Patient patient = patientService.findPatient(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient with id " + id + " does not exist"));

        return ResponseEntity.ok(patientMapper.toDto(patient));
    }

    /**
     * POST /api/patients
     *
     * Creates a new patient.
     * Validates the request body using @Valid.
     * Returns 201 Created on success.
     *
     * @param dto the patient DTO from the request body
     * @return ResponseEntity containing the created PatientDto
     */
    @PostMapping
    public ResponseEntity<PatientDto> createPatient(@Valid @RequestBody PatientDto dto) {
        Patient saved = patientService.addPatient(patientMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(patientMapper.toDto(saved));
    }

    /**
     * PUT /api/patients/{id}
     *
     * Updates an existing patient.
     * Validates the request body and checks if the patient exists.
     * Throws PatientNotFoundException if the patient does not exist.
     *
     * @param id the patient ID to update
     * @param patientDto the patient DTO from the request body
     * @return ResponseEntity containing the updated PatientDto
     * @throws PatientNotFoundException if patient not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<PatientDto> updatePatient(@PathVariable int id, @Valid @RequestBody PatientDto patientDto) throws PatientNotFoundException {
        // Verify that the patient exists
        patientService.findPatient(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient with id " + id + " does not exist"));

        // Set the ID to ensure the correct patient is updated
        patientDto.setId(id);
        Patient updated = patientService.updatePatient(patientMapper.toEntity(patientDto));
        return ResponseEntity.ok(patientMapper.toDto(updated));
    }
}
