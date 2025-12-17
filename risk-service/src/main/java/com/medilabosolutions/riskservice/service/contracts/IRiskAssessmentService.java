package com.medilabosolutions.riskservice.service.contracts;

import com.medilabosolutions.riskservice.dto.NoteDto;
import com.medilabosolutions.riskservice.dto.PatientDto;
import com.medilabosolutions.riskservice.dto.RiskResponseDto;

import java.util.List;

public interface IRiskAssessmentService {

    RiskResponseDto assessmentPatientRisk(PatientDto patient, List<NoteDto> notes);

    String assessmentRisk(PatientDto patient, List<NoteDto> notes);

}
