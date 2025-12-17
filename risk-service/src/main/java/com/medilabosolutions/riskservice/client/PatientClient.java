package com.medilabosolutions.riskservice.client;

import com.medilabosolutions.riskservice.config.FeignConfig;
import com.medilabosolutions.riskservice.dto.PatientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for communicating with the Patient Service.
 * <p>
 * This client provides access to patient information needed for risk assessment.
 */
@FeignClient(
        name = "patient-service",
        configuration = FeignConfig.class
)
public interface PatientClient {

    /**
     * Retrieves a patient by their ID.
     *
     * @param id The unique ID of the patient
     * @return {@link PatientDto} containing patient details
     */
    @GetMapping("/api/patients/{id}")
    PatientDto getPatientById(@PathVariable("id") int id);

}
