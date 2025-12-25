package com.medilabosolutions.riskservice.controller;

import com.medilabosolutions.riskservice.client.NoteClient;
import com.medilabosolutions.riskservice.client.PatientClient;
import com.medilabosolutions.riskservice.dto.NoteDto;
import com.medilabosolutions.riskservice.dto.PatientDto;
import com.medilabosolutions.riskservice.dto.RiskResponseDto;
import com.medilabosolutions.riskservice.exception.NoteServiceException;
import com.medilabosolutions.riskservice.exception.PatientNotFoundException;
import com.medilabosolutions.riskservice.exception.PatientServiceException;
import com.medilabosolutions.riskservice.exception.RiskAssessmentException;
import com.medilabosolutions.riskservice.service.RiskAssessmentServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * REST controller responsible for exposing endpoints related to patient risk assessment.
 */
@RestController
@RequestMapping("/api/risk")
public class RiskController {

    private final PatientClient patientClient;
    private final NoteClient noteClient;
    private final RiskAssessmentServiceImpl riskAssessmentService;

    public RiskController(
            PatientClient patientClient,
            NoteClient noteClient,
            RiskAssessmentServiceImpl riskAssessmentService) {

        this.patientClient = patientClient;
        this.noteClient = noteClient;
        this.riskAssessmentService = riskAssessmentService;
    }

    /**
     * Retrieves the risk assessment for a given patient.
     * @param id the unique identifier of the patient
     * @return a {@link ResponseEntity} containing the {@link RiskResponseDto}
     *
     * @throws PatientNotFoundException   if no patient is found for the given ID
     * @throws PatientServiceException    if an error occurs while calling the patient service
     * @throws NoteServiceException       if an error occurs while calling the note service
     * @throws RiskAssessmentException    if an error occurs during risk calculation
     */
    @GetMapping("/patient/{id}")
    public ResponseEntity<RiskResponseDto> getRiskAssessmentPatient(@PathVariable int id) {

        PatientDto patient;
        try {
            patient = patientClient.getPatientById(id);
        } catch (Exception e) {
            throw new PatientServiceException("Error fetching patient data");
        }

        if (patient == null) {
            throw new PatientNotFoundException(id);
        }

        List<NoteDto> notes;
        try {
            notes = noteClient.getNotesByPatientId(id);
            if (notes == null) {
                notes = Collections.emptyList();
            }
        } catch (feign.FeignException.NotFound e) {
            notes = Collections.emptyList();
        } catch (Exception e) {
            throw new NoteServiceException("Error fetching patient notes");
        }

        try {
            RiskResponseDto response =
                    riskAssessmentService.assessmentPatientRisk(patient, notes);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RiskAssessmentException("Error during risk assessment");
        }
    }
}
