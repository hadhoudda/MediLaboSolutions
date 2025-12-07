package com.medilabosolutions.riskservice.client;

import com.medilabosolutions.riskservice.config.FeignConfig;
import com.medilabosolutions.riskservice.dto.PatientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "api-patient",
        configuration = FeignConfig.class)
public interface PatientClient {

    @GetMapping("/api/patients/{id}")
    PatientDto getPatientById(@PathVariable("id") int id);
}
