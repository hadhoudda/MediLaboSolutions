package com.medilabosolutions.back_patient.controller;


import com.medilabosolutions.back_patient.exceptions.PatientNotFoundException;
import com.medilabosolutions.back_patient.model.Patient;
import com.medilabosolutions.back_patient.service.contracts.IPatientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/patient")
public class PatientController {

    private static final Logger logger = LogManager.getLogger(PatientController.class);
    private final IPatientService iPatientService;

    public PatientController(IPatientService iPatientService) {
        this.iPatientService = iPatientService;
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getPatient(){
        List<Patient> patientList = iPatientService.findAllPatients();
        if (!patientList.isEmpty()){
            return new ResponseEntity<>(patientList, HttpStatus.OK);
        } else {
            logger.info("list patientest vide");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable int id) throws PatientNotFoundException {
        Optional<Patient> patient = iPatientService.findPatient(id);
        if(!patient.isEmpty()){
            return ResponseEntity.ok(patient.get());
        } else {
            throw new PatientNotFoundException("Le patient avec l'id " + id + " n'existe pas");

        }
    }

    @PostMapping
    public ResponseEntity<?> createPatient(@RequestBody Patient patient){
        Patient patientCreate = iPatientService.addPatient(patient);
        if(patientCreate!=null){
            return ResponseEntity.ok(patientCreate);
        } else {
            logger.info("patient n'existe pas");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("patient n'est pas ajouté");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable int id, @RequestBody Patient patient) throws PatientNotFoundException {
        Optional<Patient> existingPatient = iPatientService.findPatient(patient.getId());
        if (existingPatient.isEmpty()) {
            throw new PatientNotFoundException("Le patient avec l'id " + id + " n'existe pas");
        }

        patient.setId(id);
        Patient updatedPatient = iPatientService.updatePatient(patient);
        return ResponseEntity.ok(updatedPatient);
    }

}
