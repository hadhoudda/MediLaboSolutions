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
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
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

    PatientDto patient;

    @BeforeEach
    void setup() {

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

    @Test
    void testGetRiskAssessmentPatient_patientNotFound() {
        when(patientClient.getPatientById(1)).thenReturn(null);

        ResponseEntity<?> response = controller.getRiskAssessmentPatient(1);

        assertEquals(404, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("non trouvé"));
    }

    @Test
    void testGetRiskAssessmentPatient_patientClientError() {
        when(patientClient.getPatientById(1)).thenThrow(new RuntimeException("Erreur"));

        ResponseEntity<?> response = controller.getRiskAssessmentPatient(1);

        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Erreur lors de la récupération du patient"));
    }

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

    @Test
    void testGetRiskAssessmentPatient_notesError() {
        when(patientClient.getPatientById(1)).thenReturn(patient);
        when(noteClient.getNotesByPatientId(1)).thenThrow(new RuntimeException("Erreur Notes"));

        ResponseEntity<?> response = controller.getRiskAssessmentPatient(1);

        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Erreur lors de la récupération des notes"));
    }

    @Test
    void testGetRiskAssessmentPatient_riskServiceError() {
        when(patientClient.getPatientById(1)).thenReturn(patient);
        when(noteClient.getNotesByPatientId(1)).thenReturn(List.of());

        when(riskService.assessmentPatientRisk(patient, List.of()))
                .thenThrow(new RuntimeException("Erreur Service"));

        ResponseEntity<?> response = controller.getRiskAssessmentPatient(1);

        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Erreur lors de l'évaluation du risque"));
    }
}