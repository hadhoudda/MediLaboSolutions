package com.medilabosolutions.riskservice.unitaire;

import com.medilabosolutions.riskservice.dto.NoteDto;
import com.medilabosolutions.riskservice.dto.PatientDto;
import com.medilabosolutions.riskservice.enums.RiskAssessment;
import com.medilabosolutions.riskservice.service.RiskAssessmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RiskAssessmentServiceImplTest {

    private RiskAssessmentServiceImpl riskAssessmentService;

    @BeforeEach
    void setUp() {
        riskAssessmentService = new RiskAssessmentServiceImpl();
    }

    private PatientDto patient(String gender, int age) {
        PatientDto patient = new PatientDto();
        patient.setId(1);
        patient.setGender(gender);
        patient.setDateOfBirth(LocalDate.now().minusYears(age));
        return patient;
    }

    private NoteDto note(String text) {
        NoteDto note = new NoteDto();
        note.setNote(text);
        return note;
    }

    @Test
    void testRisk1_NoNotes() {
        PatientDto p = patient("M", 50);

        String risk = riskAssessmentService.assessmentRisk(p, List.of());

        assertEquals(RiskAssessment.RISK_1.getLabel(), risk);
    }

    @Test
    void testRisk1_NoTermsFound() {
        PatientDto p = patient("F", 40);
        List<NoteDto> notes = List.of(note("Aucune information utile ici"));

        String risk = riskAssessmentService.assessmentRisk(p, notes);

        assertEquals(RiskAssessment.RISK_1.getLabel(), risk);
    }

    @Test
    void testRisk2_AgeOver30_Terms2to5() {
        PatientDto p = patient("F", 45);
        List<NoteDto> notes = List.of(
                note("Poids"),
                note("Cholesterol élevé")
        );

        String risk = riskAssessmentService.assessmentRisk(p, notes);
        assertEquals(RiskAssessment.RISK_2.getLabel(), risk);
    }

    @Test
    void testRisk3_AgeOver30_Terms6or7() {
        PatientDto patient = patient("M", 60);
        List<NoteDto> notes = List.of(
                note("hemoglobine a1c microalbumine taille poids anormal cholesterol")
        );

        String risk = riskAssessmentService.assessmentRisk(patient, notes);
        assertEquals(RiskAssessment.RISK_3.getLabel(), risk);
    }

    @Test
    void testRisk4_AgeOver30_Terms8plus() {
        PatientDto p = patient("M", 70);
        List<NoteDto> notes = List.of(
                note("hemoglobine a1c microalbumine taille poids anormal cholesterol vertige anticorps")
        );

        String risk = riskAssessmentService.assessmentRisk(p, notes);
        assertEquals(RiskAssessment.RISK_4.getLabel(), risk);
    }

    @Test
    void testRisk3_Male_AgeUnder30_Terms3() {
        PatientDto p = patient("M", 25);
        List<NoteDto> notes = List.of(
                note("hemoglobine a1c microalbumine taille")
        );

        String risk = riskAssessmentService.assessmentRisk(p, notes);
        assertEquals(RiskAssessment.RISK_3.getLabel(), risk);
    }

    @Test
    void testRisk4_Male_AgeUnder30_Terms5plus() {
        PatientDto patient = patient("M", 20);
        List<NoteDto> notes = List.of(
                note("hemoglobine a1c microalbumine taille poids anormal cholesterol")
        );

        String risk = riskAssessmentService.assessmentRisk(patient, notes);
        assertEquals(RiskAssessment.RISK_4.getLabel(), risk);
    }

    @Test
    void testRisk3_Female_AgeUnder30_Terms4() {
        PatientDto patient = patient("F", 22);
        List<NoteDto> notes = List.of(
                note("hemoglobine a1c microalbumine taille poids ")
        );

        String risk = riskAssessmentService.assessmentRisk(patient, notes);
        assertEquals(RiskAssessment.RISK_3.getLabel(), risk);
    }

    @Test
    void testRisk4_Female_AgeUnder30_Terms7plus() {
        PatientDto patient = patient("F", 27);
        List<NoteDto> notes = List.of(
                note("hemoglobine a1c microalbumine taille poids anormal cholesterol vertige")
        );

        String risk = riskAssessmentService.assessmentRisk(patient, notes);
        assertEquals(RiskAssessment.RISK_4.getLabel(), risk);
    }

    @Test
    void testAssessmentPatientRisk_ReturnsDto() {
        PatientDto patient = patient("M", 40);
        List<NoteDto> notes = List.of(note("hemoglobine a1c microalbumine "));

        var result = riskAssessmentService.assessmentPatientRisk(patient, notes);

        assertEquals(patient.getId(), result.getPatientId());
        assertEquals(40, result.getAge());
        assertEquals(RiskAssessment.RISK_2.getLabel(), result.getRiskLevel());
    }

}
