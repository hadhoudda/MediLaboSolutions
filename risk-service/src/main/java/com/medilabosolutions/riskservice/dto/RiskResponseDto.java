package com.medilabosolutions.riskservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskResponseDto {

    private Integer patientId;
    private Integer age;
    private String riskLevel;

}
