package com.medilabosolutions.frontservice.controller;

import com.medilabosolutions.frontservice.Beans.PatientBean;
import com.medilabosolutions.frontservice.Beans.RiskBean;
import com.medilabosolutions.frontservice.client.PatientGatewayClient;
import com.medilabosolutions.frontservice.client.RiskGatewayClient;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsible for managing patients.
 *
 * <p>Provides endpoints to list, view, create, and edit patients.
 * Integrates with {@link PatientGatewayClient} for patient data
 * and {@link RiskGatewayClient} for patient risk assessment.</p>
 */
@Controller
@RequestMapping
public class PatientController {

    private final PatientGatewayClient patientGatewayClient;
    private final RiskGatewayClient riskGatewayClient;

    public PatientController(PatientGatewayClient patientGatewayClient, RiskGatewayClient riskGatewayClient) {
        this.patientGatewayClient = patientGatewayClient;
        this.riskGatewayClient = riskGatewayClient;
    }

    /**
     * Displays the list of all patients.
     *
     * @param model the Spring model to pass data to the view
     * @return the view name "patient-list"
     */
    @GetMapping("/patients")
    public String listPatients(Model model) {
        List<PatientBean> patients = patientGatewayClient.getListPatient();
        model.addAttribute("patients", patients);
        return "patient-list";
    }

    /**
     * Displays detailed information for a specific patient,
     * including risk assessment.
     *
     * @param id    the patient ID
     * @param model the Spring model to pass data to the view
     * @return the view name "patient-details"
     */

    @GetMapping("/patients/{id}")
    public String getPatientDetails(@PathVariable int id, Model model) {

        try {
            PatientBean patient = patientGatewayClient.getPatientById(id);
            RiskBean risk = riskGatewayClient.getRiskPatient(id);

            if (risk == null) {
                risk = new RiskBean();
                risk.setRiskLevel("None");
            }

            model.addAttribute("patient", patient);
            model.addAttribute("risk", risk);

            return "patient-details";

        } catch (feign.FeignException.NotFound e) {
            // Patient not found → error page
            model.addAttribute("errorMessage", "Le patient avec l'identifiant " + id + " n'existe pas.");
            return "error-page";

        } catch (Exception e) {
            // Other errors
            model.addAttribute("errorMessage", "Une erreur inattendue est survenue.");
            return "error-page";
        }
    }


    /**
     * Shows the form to register a new patient.
     *
     * @param model the Spring model to pass data to the view
     * @return the view name "patient-create"
     */
    @GetMapping("/patients/add")
    public String showRegisterForm(Model model) {
        model.addAttribute("patient", new PatientBean());
        return "patient-create";
    }

    /**
     * Saves a newly created patient.
     * Validates the input and displays the form again if errors exist.
     *
     * @param patientBean    the patient data from the form
     * @param bindingResult  result of validation
     * @return redirect to "/patients" if successful, otherwise redisplay the form
     */
    @PostMapping("/patients/add")
    public String savePatient(@Valid @ModelAttribute("patient") PatientBean patientBean,
                              BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "patient-create";
        }

        patientGatewayClient.createPatient(patientBean);
        return "redirect:/patients";
    }

    /**
     * Shows the form to edit an existing patient.
     *
     * @param id    the patient ID
     * @param model the Spring model to pass data to the view
     * @return the view name "patient-edit"
     */
    @GetMapping("/patients/{id}/edit")
    public String showEditForm(@PathVariable int id, Model model) {
        PatientBean patient = patientGatewayClient.getPatientById(id);
        model.addAttribute("patient", patient);
        return "patient-edit";
    }

    /**
     * Updates an existing patient.
     * Validates the input and displays the form again if errors exist.
     *
     * @param id            the patient ID
     * @param patientBean   the updated patient data from the form
     * @param bindingResult result of validation
     * @return redirect to the patient details page if successful, otherwise redisplay the form
     */
    @PostMapping("/patients/{id}/edit")
    public String editPatient(@PathVariable int id,
                              @Valid @ModelAttribute("patient") PatientBean patientBean,
                              BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "patient-edit";
        }

        patientGatewayClient.updatePatient(id, patientBean);
        return "redirect:/patients/" + id;
    }

}
