package com.medilabosolutions.frontservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabosolutions.frontservice.Beans.PatientBean;
import com.medilabosolutions.frontservice.proxies.MicroservicePatientProxy;
import feign.FeignException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping
public class PatientController {

    private final MicroservicePatientProxy patientProxy;

    public PatientController(MicroservicePatientProxy patientProxy) {
        this.patientProxy = patientProxy;
    }

    // Liste des patients (protégée)
    @GetMapping("/patients")
    public String listPatients(Model model) {

        List<PatientBean> patients = patientProxy.getListPatient();
        model.addAttribute("patients", patients);
        return "patients";
    }

    // Détail patient (protégée)
    @GetMapping("/patients/{id}")
    public String detailPatient(@PathVariable int id, Model model) {

        PatientBean patient = patientProxy.getPatientById(id);
        model.addAttribute("patient", patient);
        return "patient";
    }

    @GetMapping("/patients/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("patient", new PatientBean());
        return "register";
    }

    @PostMapping("/patients/register")
    public String savePatient(@ModelAttribute("patient") PatientBean patientBean) {
        // sauvegarde le patient en base
        PatientBean patient =patientProxy.createPatient(patientBean);
        return "redirect:/patients";
    }


    @GetMapping("/patients/update/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        PatientBean patient = patientProxy.getPatientById(id);
        model.addAttribute("patient", patient);
        return "edit";
    }

    @PostMapping("/patients/update/{id}")
    public String editPatient(@PathVariable int id,
                              @ModelAttribute PatientBean patientBean) {

        patientProxy.updatePatient(id, patientBean);

        return "redirect:/patients/" + id;
    }


}
