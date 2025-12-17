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

/**
 * REST controller responsible for calculating and returning patient risk assessments.
 * <p>
 * This controller fetches patient information and clinical notes from external services
 * using Feign clients, and then delegates the risk calculation to {@link RiskAssessmentServiceImpl}.
 */
@RestController
@RequestMapping("/api/risk")
public class RiskController {

    private final PatientClient patientClient;
    private final NoteClient noteClient;
    private final RiskAssessmentServiceImpl riskAssessmentService;

    /**
     * Constructor for dependency injection of clients and service.
     *
     * @param patientClient           Feign client to fetch patient data
     * @param noteClient              Feign client to fetch patient notes
     * @param riskAssessmentServiceImpl Service for risk assessment calculation
     */
    public RiskController(
            PatientClient patientClient,
            NoteClient noteClient,
            RiskAssessmentServiceImpl riskAssessmentServiceImpl) {

        this.patientClient = patientClient;
        this.noteClient = noteClient;
        this.riskAssessmentService = riskAssessmentServiceImpl;
    }

    /**
     * GET endpoint to retrieve risk assessment for a patient by ID.
     * <p>
     * It fetches the patient information and clinical notes, handles errors gracefully,
     * and calculates the risk based on predefined rules.
     *
     * @param id The ID of the patient
     * @return ResponseEntity containing the {@link RiskResponseDto} or an error message
     */
    @GetMapping("/patient/{id}")
    public ResponseEntity<?> getRiskAssessmentPatient(@PathVariable int id) {

        PatientDto patient;
        try {

            patient = patientClient.getPatientById(id);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Patient with ID " + id + " not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching patient data: " + e.getMessage());
        }

        List<NoteDto> notes;
        try {

            notes = noteClient.getNotesByPatientId(id);

            // Safety check if notes are null
            if (notes == null) {
                notes = Collections.emptyList();
            }

        } catch (feign.FeignException.NotFound e) {
            // If notes not found, use empty list
            notes = Collections.emptyList();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching patient notes: " + e.getMessage());
        }

        // --- Risk calculation ---
        try {
            RiskResponseDto response = riskAssessmentService.assessmentPatientRisk(patient, notes);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during risk assessment: " + e.getMessage());
        }
    }

}
