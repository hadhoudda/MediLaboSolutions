package com.medilabosolutions.frontservice.Beans;

import lombok.Data;

@Data
public class RiskBean {

    private Integer patientId;
    private Integer age;
    private String riskLevel;

}
