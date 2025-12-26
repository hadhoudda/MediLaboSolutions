package com.medilabosolutions.riskservice.integration;

import com.medilabosolutions.riskservice.client.NoteClient;
import com.medilabosolutions.riskservice.client.PatientClient;
import com.medilabosolutions.riskservice.controller.RiskController;
import com.medilabosolutions.riskservice.dto.NoteDto;
import com.medilabosolutions.riskservice.dto.PatientDto;
import com.medilabosolutions.riskservice.dto.RiskResponseDto;
import com.medilabosolutions.riskservice.exception.NoteServiceException;
import com.medilabosolutions.riskservice.exception.PatientNotFoundException;
import com.medilabosolutions.riskservice.exception.PatientServiceException;
import com.medilabosolutions.riskservice.exception.RiskAssessmentException;
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

    @BeforeEach
    void setup() {
        // GIVEN: a sample patient for testing
        patient = new PatientDto(
                1,
                "John",
                "Doe",
                LocalDate.of(1980, 5, 10),
                "M"
        );
    }

    @Test
    void testGetRiskAssessmentPatient_success() {
        // GIVEN: patient exists and has notes
        List<NoteDto> notes = List.of(new NoteDto("1", 1, "smoker", null, null));
        RiskResponseDto risk = new RiskResponseDto(1, 44, "Borderline");

        when(patientClient.getPatientById(1)).thenReturn(patient);
        when(noteClient.getNotesByPatientId(1)).thenReturn(notes);
        when(riskService.assessmentPatientRisk(patient, notes)).thenReturn(risk);

        // WHEN: retrieving risk assessment for the patient
        ResponseEntity<?> response = controller.getRiskAssessmentPatient(1);

        // THEN: response is 200 OK and contains the correct risk assessment
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(risk, response.getBody());
    }

    @Test
    void testGetRiskAssessmentPatient_patientNotFound() {
        // GIVEN: patient does not exist
        when(patientClient.getPatientById(1)).thenReturn(null);

        // THEN: retrieving risk assessment throws PatientNotFoundException
        assertThrows(PatientNotFoundException.class, () -> controller.getRiskAssessmentPatient(1));
    }

    @Test
    void testGetRiskAssessmentPatient_patientClientError() {
        // GIVEN: patient client throws an exception
        when(patientClient.getPatientById(1)).thenThrow(new RuntimeException("Some error"));

        // THEN: retrieving risk assessment throws PatientServiceException
        assertThrows(PatientServiceException.class, () -> controller.getRiskAssessmentPatient(1));
    }

    @Test
    void testGetRiskAssessmentPatient_notesNull() {
        // GIVEN: patient exists but notes are null
        when(patientClient.getPatientById(1)).thenReturn(patient);
        when(noteClient.getNotesByPatientId(1)).thenReturn(null);

        RiskResponseDto expected = new RiskResponseDto(1, 44, "None");
        when(riskService.assessmentPatientRisk(eq(patient), anyList())).thenReturn(expected);

        // WHEN: retrieving risk assessment
        ResponseEntity<?> response = controller.getRiskAssessmentPatient(1);

        // THEN: response is 200 OK and risk assessment uses empty notes list
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expected, response.getBody());
    }

    @Test
    void testGetRiskAssessmentPatient_notesError() {
        // GIVEN: patient exists but note client throws exception
        when(patientClient.getPatientById(1)).thenReturn(patient);
        when(noteClient.getNotesByPatientId(1)).thenThrow(new RuntimeException("Notes error"));

        // THEN: retrieving risk assessment throws NoteServiceException
        assertThrows(NoteServiceException.class, () -> controller.getRiskAssessmentPatient(1));
    }

    @Test
    void testGetRiskAssessmentPatient_riskServiceError() {
        // GIVEN: patient exists, notes are empty, but risk service throws exception
        when(patientClient.getPatientById(1)).thenReturn(patient);
        when(noteClient.getNotesByPatientId(1)).thenReturn(Collections.emptyList());
        when(riskService.assessmentPatientRisk(patient, Collections.emptyList()))
                .thenThrow(new RuntimeException("Risk service error"));

        // THEN: retrieving risk assessment throws RiskAssessmentException
        assertThrows(RiskAssessmentException.class, () -> controller.getRiskAssessmentPatient(1));
    }
}
