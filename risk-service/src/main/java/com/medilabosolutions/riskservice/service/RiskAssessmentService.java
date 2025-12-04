package com.medilabosolutions.riskservice.service;

import com.medilabosolutions.riskservice.dto.NoteDto;
import com.medilabosolutions.riskservice.dto.PatientDto;
import com.medilabosolutions.riskservice.dto.RiskResponseDto;
import com.medilabosolutions.riskservice.enums.RiskAssessment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.medilabosolutions.riskservice.model.RiskTerms.listRisks;

@Slf4j
@Service
public class RiskAssessmentService {

    private static final int AGE_THRESHOLD = 30;

    public RiskResponseDto assessmentPatientRisk(PatientDto patient, List<NoteDto> notes) {

        int age = calculatePatientAge(patient.getDateOfBirth());
        String riskLabel = assessmentRisk(patient,  notes);

        return new RiskResponseDto(
                patient.getId(),
                age,
                riskLabel);
    }

    public String assessmentRisk(PatientDto patient, List<NoteDto> notes) {
        int age = calculatePatientAge(patient.getDateOfBirth());
        long riskTermsCount;

        if (notes == null || notes.isEmpty()) {
            riskTermsCount = 0;
        } else {

            // On concatène toutes les notes en un texte
            String allNotesText = notes.stream()
                    .map(n -> normalize(n.getNote()))
                    .collect(Collectors.joining(" "));


            log.info("Texte complet des notes : {}", allNotesText);

            riskTermsCount = listRisks.stream()
                    .map(this::normalize)
                    .filter(term -> Pattern.compile(term).matcher(allNotesText).find())
                    .count();

            log.info("Nombre de mots de risque trouvés : {}", riskTermsCount);
        }

        boolean isMale = "M".equals(patient.getGender());
        //risk 1
        if (riskTermsCount == 0 || notes.isEmpty()) {
            return RiskAssessment.RISK_1.getLabel();
        }
        //age > 30
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

        //age < 30
        if (age < AGE_THRESHOLD){
            if(isMale){
                if(riskTermsCount == 3){
                    return RiskAssessment.RISK_3.getLabel();
                }
                if(riskTermsCount >= 5){
                    return RiskAssessment.RISK_4.getLabel();
                }
            }else {
                if(riskTermsCount == 4){
                    return RiskAssessment.RISK_3.getLabel();
                }
                if(riskTermsCount >= 7){
                    return RiskAssessment.RISK_4.getLabel();
                }
            }
        }


        return RiskAssessment.RISK_1.getLabel();
    }

    private String normalize(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "") // supprime les accents
                .toLowerCase()
                .trim();// supprime les espaces !
    }

    private int calculatePatientAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
