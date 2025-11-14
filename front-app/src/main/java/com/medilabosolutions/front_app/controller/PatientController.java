package com.medilabosolutions.front_app.controller;

import com.medilabosolutions.front_app.Beans.PatientBean;
import com.medilabosolutions.front_app.proxies.MicroservicePatientProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping
public class PatientController {

    @Value("${app.gateway.prefix}")
    private String gatewayPrefix;

    private final MicroservicePatientProxy patientProxy;

    public PatientController(MicroservicePatientProxy patientProxy) {
        this.patientProxy = patientProxy;
    }

    @PostMapping("/patients")
    public String login(String email, String password, Model model) {
        // securise apres
        return "redirect:/patients";
    }

    @GetMapping("/patients")
    public String listPatients(Model model) {
        List<PatientBean> patients = patientProxy.getListPatient();
        model.addAttribute("patients", patients);
        model.addAttribute("gatewayPrefix", gatewayPrefix);
        return "patients";
    }

    @GetMapping("/patients/{id}")
    public String detailPatient(@PathVariable int id, Model model) {
        PatientBean patient = patientProxy.getPatientById(id);
        model.addAttribute("patient", patient);
        return "patient";
    }

    @PutMapping("/patient/edit/{id}")
    public String editPatient(@PathVariable int id, @RequestBody PatientBean patientBean, Model model){
        PatientBean patientEdit = patientProxy.updatePatient(id, patientBean);
        model.addAttribute("patient", patientEdit);
        return "edit";
    }

}
