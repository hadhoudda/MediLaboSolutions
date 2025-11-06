package com.medilabosolutions.back_patient.service;


import com.medilabosolutions.back_patient.model.Patient;
import com.medilabosolutions.back_patient.repository.PatientRepository;
import com.medilabosolutions.back_patient.service.contracts.IPatientService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService implements IPatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public List<Patient> findAllPatients(){
        return patientRepository.findAll();
    }

    @Override
    public Optional<Patient> findPatient(int id){
        return patientRepository.findById(id);
    }

    @Override
    public Patient addPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient updatePatient(Patient patient){
        return patientRepository.save(patient);
    }

    @Override
    public void deletePatient(int id){
        patientRepository.deleteById(id);
    }
}
