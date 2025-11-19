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

    @GetMapping("/patient")
    List<PatientBean> getListPatient();

    @GetMapping("/patient/{id}")
    PatientBean getPatientById(@PathVariable("id") int id);

    @PostMapping("/patient")
    PatientBean createPatient(@RequestBody PatientBean patientBean);

    @PutMapping("/patient/{id}")
    PatientBean updatePatient(@PathVariable int id, @RequestBody PatientBean patientBean);
}
