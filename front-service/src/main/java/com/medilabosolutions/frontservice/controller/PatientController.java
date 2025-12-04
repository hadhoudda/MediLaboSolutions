package com.medilabosolutions.frontservice.controller;

import com.medilabosolutions.frontservice.Beans.PatientBean;
import com.medilabosolutions.frontservice.client.PatientGatewayClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping
public class PatientController {

    private final PatientGatewayClient patientProxy;

    public PatientController(PatientGatewayClient patientProxy) {
        this.patientProxy = patientProxy;
    }

    // Liste des patients (protégée)
    @GetMapping("/patients")
    public String listPatients(Model model) {

        List<PatientBean> patients = patientProxy.getListPatient();
        model.addAttribute("patients", patients);
        return "patient-list";
    }

    // Détail patient (protégée)
    @GetMapping("/patients/{id}")
    public String detailPatient(@PathVariable int id, Model model) {

        PatientBean patient = patientProxy.getPatientById(id);
        model.addAttribute("patient", patient);
        return "patient-details";
    }

    @GetMapping("/patients/add")
    public String showRegisterForm(Model model) {
        model.addAttribute("patient", new PatientBean());
        return "patient-create";
    }

    @PostMapping("/patients/add")
    public String savePatient(@ModelAttribute("patient") PatientBean patientBean) {
        // sauvegarde le patient en base
        PatientBean patient =patientProxy.createPatient(patientBean);
        return "redirect:/patients";
    }


    @GetMapping("/patients/{id}/edit")
    public String showEditForm(@PathVariable int id, Model model) {
        PatientBean patient = patientProxy.getPatientById(id);
        model.addAttribute("patient", patient);
        return "patient-edit";
    }

    @PostMapping("/patients/{id}/edit")
    public String editPatient(@PathVariable int id,
                              @ModelAttribute PatientBean patientBean) {

        patientProxy.updatePatient(id, patientBean);

        return "redirect:/patients/" + id;
    }


}
