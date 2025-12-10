package com.medilabosolutions.riskservice.controller;

import com.medilabosolutions.riskservice.client.NoteClient;
import com.medilabosolutions.riskservice.client.PatientClient;
import com.medilabosolutions.riskservice.dto.NoteDto;
import com.medilabosolutions.riskservice.dto.PatientDto;
import com.medilabosolutions.riskservice.dto.RiskResponseDto;
import com.medilabosolutions.riskservice.service.RiskAssessmentServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/risk")
public class RiskController {

    private final PatientClient patientClient;
    private final NoteClient noteClient;
    private final RiskAssessmentServiceImpl riskAssessmentService;

    public RiskController(
            PatientClient patientClient,
            NoteClient noteClient,
            RiskAssessmentServiceImpl riskAssessmentServiceImpl) {

        this.patientClient = patientClient;
        this.noteClient = noteClient;
        this.riskAssessmentService = riskAssessmentServiceImpl;
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<?> getRiskAssessmentPatient(@PathVariable int id) {

        PatientDto patient;
        try {
            patient = patientClient.getPatientById(id);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Patient avec l'ID " + id + " non trouvé.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération du patient : " + e.getMessage());
        }
        List<NoteDto> notes;

        try {
            notes = noteClient.getNotesByPatientId(id);

            // Sécurité si null
            if (notes == null) {
                notes = Collections.emptyList();
            }

        } catch (feign.FeignException.NotFound e) {
            // liste vide
            notes = Collections.emptyList();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération des notes : " + e.getMessage());
        }

        // --- Risk calculation ---
        try {
            RiskResponseDto response = riskAssessmentService.assessmentPatientRisk(patient, notes);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'évaluation du risque : " + e.getMessage());
        }
    }

}
