package com.medilabosolutions.frontservice.controller;

import com.medilabosolutions.frontservice.Beans.PatientBean;
import com.medilabosolutions.frontservice.Beans.RiskBean;
import com.medilabosolutions.frontservice.client.PatientGatewayClient;
import com.medilabosolutions.frontservice.client.RiskGatewayClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping
public class PatientController {

    private final PatientGatewayClient patientGatewayClient;

    public PatientController(PatientGatewayClient patientGatewayClient, RiskGatewayClient riskGatewayClient) {
        this.patientGatewayClient = patientGatewayClient;
        this.riskGatewayClient = riskGatewayClient;
    }

    private final RiskGatewayClient riskGatewayClient;



    // Liste des patients (protégée)
    @GetMapping("/patients")
    public String listPatients(Model model) {

        List<PatientBean> patients = patientGatewayClient.getListPatient();
        model.addAttribute("patients", patients);
        return "patient-list";
    }

    // Détail patient (protégée)
    @GetMapping("/patients/{id}")
    public String getPatientDetails(@PathVariable int id, Model model) {
        PatientBean patient = patientGatewayClient.getPatientById(id);

        RiskBean risk = riskGatewayClient.getRiskPatient(id);

        if (risk == null) {
            risk = new RiskBean();
            risk.setRiskLevel("Non disponible");
        }

        model.addAttribute("patient", patient);
        model.addAttribute("risk", risk);
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
        PatientBean patient = patientGatewayClient.createPatient(patientBean);
        return "redirect:/patients";
    }


    @GetMapping("/patients/{id}/edit")
    public String showEditForm(@PathVariable int id, Model model) {
        PatientBean patient = patientGatewayClient.getPatientById(id);
        model.addAttribute("patient", patient);
        return "patient-edit";
    }

    @PostMapping("/patients/{id}/edit")
    public String editPatient(@PathVariable int id,
                              @ModelAttribute PatientBean patientBean) {

        patientGatewayClient.updatePatient(id, patientBean);

        return "redirect:/patients/" + id;
    }


}
