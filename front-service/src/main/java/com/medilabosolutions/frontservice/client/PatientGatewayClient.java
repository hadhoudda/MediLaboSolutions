package com.medilabosolutions.frontservice.client;

import com.medilabosolutions.frontservice.Beans.PatientBean;
import com.medilabosolutions.frontservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "patient-service",
        configuration = FeignConfig.class)
public interface PatientGatewayClient {

    @GetMapping("/api/patients")
    List<PatientBean> getListPatient();

    @GetMapping("/api/patients/{id}")
    PatientBean getPatientById(@PathVariable("id") int id);

    @PostMapping("/api/patients")
    PatientBean createPatient(@RequestBody PatientBean patientBean);

    @PutMapping("/api/patients/{id}")
    void updatePatient(@PathVariable int id, @RequestBody PatientBean patientBean);

}
