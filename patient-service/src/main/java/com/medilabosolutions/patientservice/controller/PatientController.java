package com.medilabosolutions.patientservice.controller;

import com.medilabosolutions.patientservice.dto.PatientDto;
import com.medilabosolutions.patientservice.exceptions.PatientNotFoundException;
import com.medilabosolutions.patientservice.mapper.PatientMapper;
import com.medilabosolutions.patientservice.model.Patient;
import com.medilabosolutions.patientservice.service.contracts.IPatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
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
    public ResponseEntity<PatientDto> createPatient(@Valid @RequestBody PatientDto dto) {
        Patient saved = patientService.addPatient(mapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDto> updatePatient(@PathVariable int id, @Valid @RequestBody PatientDto dto) throws PatientNotFoundException {
        patientService.findPatient(id)
                .orElseThrow(() -> new PatientNotFoundException("Le patient avec l'id " + id + " n'existe pas"));

        dto.setId(id);
        Patient updated = patientService.updatePatient(mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toDto(updated));
    }

}
