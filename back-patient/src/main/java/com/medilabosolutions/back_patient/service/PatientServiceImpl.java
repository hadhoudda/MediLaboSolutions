package com.medilabosolutions.back_patient.service;


import com.medilabosolutions.back_patient.model.Patient;
import com.medilabosolutions.back_patient.repository.PatientRepository;
import com.medilabosolutions.back_patient.service.contracts.IPatientService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements IPatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
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

        Assert.isNull(patient.getId(), "Not Id on save");
        return patientRepository.save(patient);
    }

    @Override
    public Patient updatePatient(Patient patient){
        Assert.isNull(patient.getId(), "Not Id on save");
        return patientRepository.save(patient);
    }

}
