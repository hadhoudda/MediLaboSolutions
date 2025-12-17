package com.medilabosolutions.riskservice.service;

import com.medilabosolutions.riskservice.dto.NoteDto;
import com.medilabosolutions.riskservice.dto.PatientDto;
import com.medilabosolutions.riskservice.dto.RiskResponseDto;
import com.medilabosolutions.riskservice.enums.RiskAssessment;
import com.medilabosolutions.riskservice.service.contracts.IRiskAssessmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.medilabosolutions.riskservice.model.RiskTerms.listRisks;

/**
 * Service implementation for assessing patient health risk based on age, gender, and medical notes.
 */
@Slf4j
@Service
public class RiskAssessmentServiceImpl implements IRiskAssessmentService {

    private static final int AGE_THRESHOLD = 30;  // Age threshold for risk categorization

    /**
     * Assess the risk level of a patient based on their age, gender, and clinical notes.
     *
     * @param patient the patient data
     * @param notes   the list of clinical notes associated with the patient
     * @return a RiskResponseDto containing patient ID, age, and risk level label
     */
    public RiskResponseDto assessmentPatientRisk(PatientDto patient, List<NoteDto> notes) {
        int age = calculatePatientAge(patient.getDateOfBirth());
        String riskLabel = assessmentRisk(patient, notes);

        return new RiskResponseDto(
                patient.getId(),
                age,
                riskLabel);
    }

    /**
     * Determines the risk label for a patient.
     * The assessment is based on the number of risk-related terms in notes,
     * age, and gender of the patient.
     *
     * @param patient the patient data
     * @param notes   the list of clinical notes
     * @return the risk label as a string
     */
    public String assessmentRisk(PatientDto patient, List<NoteDto> notes) {
        int age = calculatePatientAge(patient.getDateOfBirth());
        long riskTermsCount;

        if (notes == null || notes.isEmpty()) {
            riskTermsCount = 0;
        } else {
            // Concatenate all notes into a single text string
            String allNotesText = notes.stream()
                    .map(n -> normalize(n.getNote()))
                    .collect(Collectors.joining(" "));

            log.info("Complete notes text: {}", allNotesText);

            // Count occurrences of risk-related terms in notes
            riskTermsCount = listRisks.stream()
                    .map(this::normalize)
                    .filter(term -> Pattern.compile(term).matcher(allNotesText).find())
                    .count();

            log.info("Number of risk terms found: {}", riskTermsCount);
        }

        boolean isMale = "M".equals(patient.getGender());

        // Risk 1: no notes or no risk terms
        if (riskTermsCount == 0 || notes.isEmpty()) {
            return RiskAssessment.RISK_1.getLabel();
        }

        // Age above threshold
        if (age > AGE_THRESHOLD) {
            if (riskTermsCount >= 2 && riskTermsCount <= 5) {
                return RiskAssessment.RISK_2.getLabel();
            }
            if (riskTermsCount == 6 || riskTermsCount == 7) {
                return RiskAssessment.RISK_3.getLabel();
            }
            if (riskTermsCount >= 8) {
                return RiskAssessment.RISK_4.getLabel();
            }
        }

        // Age below threshold
        if (age < AGE_THRESHOLD) {
            if (isMale) {
                if (riskTermsCount == 3) {
                    return RiskAssessment.RISK_3.getLabel();
                }
                if (riskTermsCount >= 5) {
                    return RiskAssessment.RISK_4.getLabel();
                }
            } else {  // Female patients
                if (riskTermsCount == 4) {
                    return RiskAssessment.RISK_3.getLabel();
                }
                if (riskTermsCount >= 7) {
                    return RiskAssessment.RISK_4.getLabel();
                }
            }
        }

        // Default risk if no other conditions matched
        return RiskAssessment.RISK_1.getLabel();
    }

    /**
     * Normalizes text by removing accents, converting to lowercase, and trimming whitespace.
     *
     * @param text the input text
     * @return normalized text
     */
    private String normalize(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "") // remove accents
                .toLowerCase()
                .trim();                  // remove leading/trailing spaces
    }

    /**
     * Calculates the age of a patient based on their birth date.
     *
     * @param birthDate the patient's birth date
     * @return the age in years
     */
    private int calculatePatientAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
