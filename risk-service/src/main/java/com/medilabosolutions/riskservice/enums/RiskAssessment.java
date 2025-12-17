package com.medilabosolutions.riskservice.enums;

import lombok.Getter;

@Getter
public enum RiskAssessment {
    RISK_1("None"),
    RISK_2("Borderline"),
    RISK_3("InDanger"),
    RISK_4("EarlyOnset");

    private final String label;

    RiskAssessment(String label) {
        this.label = label;
    }

}