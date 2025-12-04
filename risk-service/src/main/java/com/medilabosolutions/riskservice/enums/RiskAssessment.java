package com.medilabosolutions.riskservice.enums;

//public enum RiskAssessment {
//    RISK_1,
//    RISK_2,
//    RISK_3,
//    RISK_4
//}
public enum RiskAssessment {
    RISK_1("None"),
    RISK_2("Borderline"),
    RISK_3("InDanger"),
    RISK_4("EarlyOnset");

    private final String label;

    RiskAssessment(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}