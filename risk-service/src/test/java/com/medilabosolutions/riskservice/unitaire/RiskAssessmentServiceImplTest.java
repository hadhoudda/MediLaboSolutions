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

/**
 * Unit tests for {@link RiskAssessmentServiceImpl}.
 * <p>
 * Tests the patient risk assessment logic considering age, gender, and medical notes.
 * Each test covers a specific risk scenario according to the predefined rules.
 */
public class RiskAssessmentServiceImplTest {

    private RiskAssessmentServiceImpl riskAssessmentService;

    /**
     * Initialize the RiskAssessmentServiceImpl before each test.
     */
    @BeforeEach
    void setUp() {
        riskAssessmentService = new RiskAssessmentServiceImpl();
    }

    /**
     * Utility method to create a patient with a specified gender and age.
     *
     * @param gender Patient gender ("M" or "F")
     * @param age    Patient age in years
     * @return PatientDto object
     */
    private PatientDto patient(String gender, int age) {
        PatientDto patient = new PatientDto();
        patient.setId(1);
        patient.setGender(gender);
        patient.setDateOfBirth(LocalDate.now().minusYears(age));
        return patient;
    }

    /**
     * Utility method to create a note with given text.
     *
     * @param text Note content
     * @return NoteDto object
     */
    private NoteDto note(String text) {
        NoteDto note = new NoteDto();
        note.setNote(text);
        return note;
    }

    @Test
    void testRisk1_NoNotes() {
        // GIVEN a patient with no notes
        PatientDto p = patient("M", 50);

        // WHEN calculating risk
        String risk = riskAssessmentService.assessmentRisk(p, List.of());

        // THEN risk should be RISK_1
        assertEquals(RiskAssessment.RISK_1.getLabel(), risk);
    }

    @Test
    void testRisk1_NoTermsFound() {
        // GIVEN notes that contain no risk terms
        PatientDto p = patient("F", 40);
        List<NoteDto> notes = List.of(note("No relevant medical information"));

        // WHEN calculating risk
        String risk = riskAssessmentService.assessmentRisk(p, notes);

        // THEN risk should be RISK_1
        assertEquals(RiskAssessment.RISK_1.getLabel(), risk);
    }

    @Test
    void testRisk2_AgeOver30_Terms2to5() {
        // GIVEN patient age > 30 and 2 risk terms in notes
        PatientDto p = patient("F", 45);
        List<NoteDto> notes = List.of(
                note("poids anormal "),
                note("Cholesterol elevated")
        );

        // WHEN calculating risk
        String risk = riskAssessmentService.assessmentRisk(p, notes);

        // THEN risk should be RISK_2
        assertEquals(RiskAssessment.RISK_2.getLabel(), risk);
    }

    @Test
    void testRisk3_AgeOver30_Terms6or7() {
        // GIVEN patient age > 30 and 6 risk terms
        PatientDto patient = patient("M", 60);
        List<NoteDto> notes = List.of(
                note("hemoglobine a1c microalbumine taille poids anormal cholesterol")
        );

        // WHEN calculating risk
        String risk = riskAssessmentService.assessmentRisk(patient, notes);

        // THEN risk should be RISK_3
        assertEquals(RiskAssessment.RISK_3.getLabel(), risk);
    }

    @Test
    void testRisk4_AgeOver30_Terms8plus() {
        // GIVEN patient age > 30 and 8+ risk terms
        PatientDto p = patient("M", 70);
        List<NoteDto> notes = List.of(
                note("hemoglobine a1c microalbumine taille poids anormal cholesterol vertige anticorps")
        );

        // WHEN calculating risk
        String risk = riskAssessmentService.assessmentRisk(p, notes);

        // THEN risk should be RISK_4
        assertEquals(RiskAssessment.RISK_4.getLabel(), risk);
    }

    @Test
    void testRisk3_Male_AgeUnder30_Terms3() {
        // GIVEN male patient < 30 with exactly 3 risk terms
        PatientDto p = patient("M", 25);
        List<NoteDto> notes = List.of(
                note("hemoglobine a1c microalbumine taille")
        );

        String risk = riskAssessmentService.assessmentRisk(p, notes);

        assertEquals(RiskAssessment.RISK_3.getLabel(), risk);
    }

    @Test
    void testRisk4_Male_AgeUnder30_Terms5plus() {
        // GIVEN male patient < 30 with 5+ risk terms
        PatientDto patient = patient("M", 20);
        List<NoteDto> notes = List.of(
                note("hemoglobine a1c microalbumine taille poids anormal cholesterol")
        );

        String risk = riskAssessmentService.assessmentRisk(patient, notes);

        assertEquals(RiskAssessment.RISK_4.getLabel(), risk);
    }

    @Test
    void testRisk3_Female_AgeUnder30_Terms4() {
        // GIVEN female patient < 30 with exactly 4 risk terms
        PatientDto patient = patient("F", 22);
        List<NoteDto> notes = List.of(
                note("hemoglobine a1c microalbumine taille poids")
        );

        String risk = riskAssessmentService.assessmentRisk(patient, notes);

        assertEquals(RiskAssessment.RISK_3.getLabel(), risk);
    }

    @Test
    void testRisk4_Female_AgeUnder30_Terms7plus() {
        // GIVEN female patient < 30 with 7+ risk terms
        PatientDto patient = patient("F", 27);
        List<NoteDto> notes = List.of(
                note("hemoglobine a1c microalbumine taille poids anormal cholesterol vertige")
        );

        String risk = riskAssessmentService.assessmentRisk(patient, notes);

        assertEquals(RiskAssessment.RISK_4.getLabel(), risk);
    }

    @Test
    void testAssessmentPatientRisk_ReturnsDto() {
        // GIVEN patient and some notes
        PatientDto patient = patient("M", 40);
        List<NoteDto> notes = List.of(note("hemoglobine a1c microalbumine"));

        // WHEN calculating full risk assessment DTO
        var result = riskAssessmentService.assessmentPatientRisk(patient, notes);

        // THEN the DTO should have correct patient ID, age, and risk level
        assertEquals(patient.getId(), result.getPatientId());
        assertEquals(40, result.getAge());
        assertEquals(RiskAssessment.RISK_2.getLabel(), result.getRiskLevel());
    }
}
