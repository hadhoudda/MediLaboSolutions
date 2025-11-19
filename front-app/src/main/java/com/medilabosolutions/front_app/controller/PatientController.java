package com.medilabosolutions.front_app.controller;

import com.medilabosolutions.front_app.Beans.PatientBean;
import com.medilabosolutions.front_app.proxies.MicroservicePatientProxy;
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
    public String listPatients(Model model, HttpSession session) {
        if (!isLogged(session)) return "redirect:/login";

        List<PatientBean> patients = patientProxy.getListPatient();
        model.addAttribute("patients", patients);
        return "patients";
    }

    // Détail patient (protégée)
    @GetMapping("/patients/{id}")
    public String detailPatient(@PathVariable int id, Model model, HttpSession session) {
        if (!isLogged(session)) return "redirect:/login";

        PatientBean patient = patientProxy.getPatientById(id);
        model.addAttribute("patient", patient);
        return "patient";
    }

    // Formulaire d’enregistrement (protégé)
    @GetMapping("/patient/register")
    public String showRegisterForm(HttpSession session) {
        if (!isLogged(session)) return "redirect:/login";
        return "register";
    }

    @PostMapping("/patient/register")
    public String registerPatient(@RequestBody PatientBean patientBean, Model model, HttpSession session){
        if (!isLogged(session)) return "redirect:/login";

        PatientBean patientRegister = patientProxy.createPatient(patientBean);
        model.addAttribute("patient", patientRegister);
        return "register";
    }

    // Formulaire d’édition (protégé)
    @GetMapping("/patient/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model, HttpSession session) {
        if (!isLogged(session)) return "redirect:/login";

        PatientBean patient = patientProxy.getPatientById(id);
        model.addAttribute("patient", patient);
        return "edit";
    }

    @PutMapping("/patient/edit/{id}")
    public String editPatient(@PathVariable int id, @RequestBody PatientBean patientBean, Model model, HttpSession session){
        if (!isLogged(session)) return "redirect:/login";

        PatientBean patientEdit = patientProxy.updatePatient(id, patientBean);
        model.addAttribute("patient", patientEdit);
        return "edit";
    }

    // Méthode utilitaire pour vérifier la connexion
    private boolean isLogged(HttpSession session) {
        Boolean logged = (Boolean) session.getAttribute("isLogged");
        return logged != null && logged;
    }
}
