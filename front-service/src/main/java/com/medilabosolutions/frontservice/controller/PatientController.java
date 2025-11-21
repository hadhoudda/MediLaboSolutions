package com.medilabosolutions.frontservice.controller;

import com.medilabosolutions.frontservice.Beans.PatientBean;
import com.medilabosolutions.frontservice.proxies.MicroservicePatientProxy;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/patients/register")
    public String registerPatient(@RequestBody PatientBean patientBean, Model model){
        PatientBean patientRegister = patientProxy.createPatient(patientBean);
        model.addAttribute("patient", patientRegister);
        return "register";
    }


    @GetMapping("/patients/update/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        PatientBean patient = patientProxy.getPatientById(id);
        model.addAttribute("patient", patient);
        return "edit";
    }

//    @PutMapping("/patients/update/{id}")
//    public String editPatient(@PathVariable int id, @RequestBody PatientBean patientBean, Model model){
//
//        PatientBean patientEdit = patientProxy.updatePatient(id, patientBean);
//        model.addAttribute("patient", patientEdit);
//        return "edit";
//    }
    @PostMapping("/patients/update/{id}")
    public String editPatient(@PathVariable int id,
                              @ModelAttribute PatientBean patientBean) {

        patientProxy.updatePatient(id, patientBean);

        return "redirect:/patients/" + id;
    }

}
