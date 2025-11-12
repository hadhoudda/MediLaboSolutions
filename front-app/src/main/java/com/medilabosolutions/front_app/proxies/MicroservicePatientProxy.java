package com.medilabosolutions.front_app.proxies;

import com.medilabosolutions.front_app.Beans.PatientBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "back-patient")
public interface MicroservicePatientProxy {

    @GetMapping("/patient")
    ResponseEntity<List<PatientBean>> getListPatient();

    @GetMapping("/patient/{id}")
    ResponseEntity<PatientBean> getPatientById(@PathVariable("id") int id);
}
