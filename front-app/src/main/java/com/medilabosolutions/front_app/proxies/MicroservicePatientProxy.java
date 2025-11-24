package com.medilabosolutions.front_app.proxies;

import com.medilabosolutions.front_app.Beans.AuthRequest;
import com.medilabosolutions.front_app.Beans.PatientBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "back-patient")
public interface MicroservicePatientProxy {

    // Login (pas besoin de JWT)
    @PostMapping("/auth/login")
    ResponseEntity<String> login(@RequestBody AuthRequest authRequest);

    // Tous les appels suivants nécessitent le JWT dans le header
    @GetMapping("/patient")
    List<PatientBean> getListPatient(@RequestHeader("Authorization") String jwt);

    @GetMapping("/patient/{id}")
    PatientBean getPatientById(@PathVariable("id") int id,
                               @RequestHeader("Authorization") String jwt);

    @PostMapping("/patient")
    PatientBean createPatient(@RequestBody PatientBean patientBean,
                              @RequestHeader("Authorization") String jwt);

    @PutMapping("/patient/{id}")
    PatientBean updatePatient(@PathVariable int id,
                              @RequestBody PatientBean patientBean,
                              @RequestHeader("Authorization") String jwt);
}
