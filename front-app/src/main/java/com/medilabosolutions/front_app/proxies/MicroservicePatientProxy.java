package com.medilabosolutions.front_app.proxies;

import com.medilabosolutions.front_app.Beans.PatientBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "back-patient")
public interface MicroservicePatientProxy {

//    @PostMapping("/login")
//    ResponseEntity<>

    @GetMapping("/patients")
    List<PatientBean> getListPatient();

    @GetMapping("/patients/{id}")
    PatientBean getPatientById(@PathVariable("id") int id);

    @PostMapping("/patients")
    PatientBean createPatient(@RequestBody PatientBean patientBean);

    @PutMapping("/patients/{id}")
    PatientBean updatePatient(@PathVariable int id, @RequestBody PatientBean patientBean);
}
