package com.medilabosolutions.patientservice.service;

import com.medilabosolutions.patientservice.model.Patient;
import com.medilabosolutions.patientservice.repository.PatientRepository;
import com.medilabosolutions.patientservice.service.contracts.IPatientService;
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
    public List<Patient> findAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Optional<Patient> findPatient(int id) {
        return patientRepository.findById(id);
    }

    @Override
    public Patient addPatient(Patient patient) {
        Assert.isTrue(patient.getId() == null, "Le patient ne doit pas avoir d'ID lors de la création");
        return patientRepository.save(patient);
    }

    @Override
    public Patient updatePatient(Patient patient) {
        Assert.notNull(patient.getId(), "L'ID est requis pour la mise à jour");
        return patientRepository.save(patient);
    }

}
