package com.medilabosolutions.back_patient.controller;

import com.medilabosolutions.back_patient.dto.PatientDto;
import com.medilabosolutions.back_patient.exceptions.PatientNotFoundException;
import com.medilabosolutions.back_patient.mapper.PatientMapper;
import com.medilabosolutions.back_patient.model.Patient;
import com.medilabosolutions.back_patient.service.contracts.IPatientService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {

    private static final Logger logger = LogManager.getLogger(PatientController.class);
    private final IPatientService patientService;
    private final PatientMapper mapper;

    @GetMapping
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        var patients = patientService.findAllPatients();

        if (patients.isEmpty()) {
            logger.info("La liste des patients est vide.");
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(mapper.toDtoList(patients));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable int id) throws PatientNotFoundException {
        Patient patient = patientService.findPatient(id)
                .orElseThrow(() -> new PatientNotFoundException("Le patient avec l'id " + id + " n'existe pas"));

        return ResponseEntity.ok(mapper.toDto(patient));
    }

    @PostMapping
    public ResponseEntity<PatientDto> createPatient(@RequestBody PatientDto dto) {
        Patient saved = patientService.addPatient(mapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDto> updatePatient(@PathVariable int id, @RequestBody PatientDto dto) throws PatientNotFoundException {
        patientService.findPatient(id)
                .orElseThrow(() -> new PatientNotFoundException("Le patient avec l'id " + id + " n'existe pas"));

        dto.setId(id);
        Patient updated = patientService.updatePatient(mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toDto(updated));
    }

}
