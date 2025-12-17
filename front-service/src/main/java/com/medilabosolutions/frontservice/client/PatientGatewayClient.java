package com.medilabosolutions.frontservice.client;

import com.medilabosolutions.frontservice.Beans.PatientBean;
import com.medilabosolutions.frontservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Feign client used to communicate with the Patient microservice.
 * Provides operations for retrieving, creating and updating patients.
 */
@FeignClient(
        name = "patient-service",
        configuration = FeignConfig.class
)
public interface PatientGatewayClient {

    /**
     * Retrieves the list of all patients.
     *
     * @return a list of patients
     */
    @GetMapping("/api/patients")
    List<PatientBean> getListPatient();

    /**
     * Retrieves a patient by its identifier.
     *
     * @param id the patient identifier
     * @return the corresponding patient
     */
    @GetMapping("/api/patients/{id}")
    PatientBean getPatientById(@PathVariable("id") int id);

    /**
     * Creates a new patient.
     *
     * @param patientBean the patient data to create
     * @return the created patient
     */
    @PostMapping("/api/patients")
    PatientBean createPatient(@RequestBody PatientBean patientBean);

    /**
     * Updates an existing patient.
     *
     * @param id the patient identifier
     * @param patientBean the updated patient data
     */
    @PutMapping("/api/patients/{id}")
    void updatePatient(@PathVariable int id, @RequestBody PatientBean patientBean);

}
