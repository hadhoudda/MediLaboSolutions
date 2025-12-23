package com.medilabosolutions.riskservice.integration;

import com.medilabosolutions.riskservice.client.NoteClient;
import com.medilabosolutions.riskservice.client.PatientClient;
import com.medilabosolutions.riskservice.controller.RiskController;
import com.medilabosolutions.riskservice.dto.NoteDto;
import com.medilabosolutions.riskservice.dto.PatientDto;
import com.medilabosolutions.riskservice.dto.RiskResponseDto;
import com.medilabosolutions.riskservice.service.RiskAssessmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Integration tests for RiskController using Mockito.
 * <p>
 * Tests various scenarios for retrieving patient risk assessments,
 * including successful retrieval, patient not found, null notes,
 * and service errors.
 */
@ExtendWith(MockitoExtension.class)
class RiskControllerTest {

    @Mock
    PatientClient patientClient;

    @Mock
    NoteClient noteClient;

    @Mock
    RiskAssessmentServiceImpl riskService;

    @InjectMocks
    RiskController controller;

    private PatientDto patient;

    /**
     * Setup common test data before each test.
     */
    @BeforeEach
    void setup() {
        // Create a sample patient for testing
        patient = new PatientDto(
                1,
                "John",
                "Doe",
                LocalDate.of(1980, 5, 10),
                "M"
        );
    }

    /**
     * Test successful risk assessment retrieval.
     */
    @Test
    void testGetRiskAssessmentPatient_success() {
        // GIVEN
        List<NoteDto> notes = List.of(
                new NoteDto("1", 1, "smoker", null, null)
        );
        RiskResponseDto risk = new RiskResponseDto(1, 44, "Borderline");

        when(patientClient.getPatientById(1)).thenReturn(patient);
        when(noteClient.getNotesByPatientId(1)).thenReturn(notes);
        when(riskService.assessmentPatientRisk(patient, notes)).thenReturn(risk);

        // WHEN
        ResponseEntity<?> response = controller.getRiskAssessmentPatient(1);

        // THEN
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(risk, response.getBody());
    }

    /**
     * Test scenario where the patient is not found.
     */
    @Test
    void testGetRiskAssessmentPatient_patientNotFound() {
        when(patientClient.getPatientById(1)).thenReturn(null);

        ResponseEntity<?> response = controller.getRiskAssessmentPatient(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Patient with ID 1 not found.", response.getBody());
    }

    /**
     * Test scenario where the patient client throws an exception.
     */
    @Test
    void testGetRiskAssessmentPatient_patientClientError() {
        when(patientClient.getPatientById(1)).thenThrow(new RuntimeException("Some error"));

        ResponseEntity<?> response = controller.getRiskAssessmentPatient(1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error fetching patient data"));
    }

    /**
     * Test scenario where notes are null (should be treated as empty list).
     */
    @Test
    void testGetRiskAssessmentPatient_notesNull() {
        when(patientClient.getPatientById(1)).thenReturn(patient);
        when(noteClient.getNotesByPatientId(1)).thenReturn(null);

        RiskResponseDto expected = new RiskResponseDto(1, 44, "None");

        when(riskService.assessmentPatientRisk(eq(patient), anyList()))
                .thenReturn(expected);

        ResponseEntity<?> response = controller.getRiskAssessmentPatient(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expected, response.getBody());
    }

    /**
     * Test scenario where the note client throws an exception.
     */
    @Test
    void testGetRiskAssessmentPatient_notesError() {
        when(patientClient.getPatientById(1)).thenReturn(patient);
        when(noteClient.getNotesByPatientId(1)).thenThrow(new RuntimeException("Notes error"));

        ResponseEntity<?> response = controller.getRiskAssessmentPatient(1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error fetching patient notes"));
    }

    /**
     * Test scenario where the risk service throws an exception.
     */
    @Test
    void testGetRiskAssessmentPatient_riskServiceError() {
        when(patientClient.getPatientById(1)).thenReturn(patient);
        when(noteClient.getNotesByPatientId(1)).thenReturn(Collections.emptyList());
        when(riskService.assessmentPatientRisk(patient, Collections.emptyList()))
                .thenThrow(new RuntimeException("Risk service error"));

        ResponseEntity<?> response = controller.getRiskAssessmentPatient(1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error during risk assessment"));
    }
}
