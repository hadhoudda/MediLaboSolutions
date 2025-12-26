package com.medilabosolutions.patientservice.service;

import com.medilabosolutions.patientservice.model.Patient;
import com.medilabosolutions.patientservice.repository.PatientRepository;
import com.medilabosolutions.patientservice.service.contracts.IPatientService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation responsible for patient management.
 */
@Service
public class PatientServiceImpl implements IPatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * Retrieves all patients.
     *
     * @return a list of all registered patients
     */
    @Override
    public List<Patient> findAllPatients() {
        return patientRepository.findAll();
    }

    /**
     * Finds a patient by its identifier.
     *
     * @param id patient identifier
     * @return an {@link Optional} containing the patient if found,
     *         or empty if no patient exists with the given id
     */
    @Override
    public Optional<Patient> findPatient(int id) {
        return patientRepository.findById(id);
    }

    /**
     * Creates a new patient.
     *
     * @param patient patient to be created
     * @return the persisted patient entity
     * @throws IllegalArgumentException if the patient already has an id
     */
    @Override
    public Patient addPatient(Patient patient) {
        // Ensure the patient is new (no existing ID)
        Assert.isTrue(patient.getId() == null,
                "Le patient ne doit pas avoir d'ID");

        return patientRepository.save(patient);
    }

    /**
     * Updates an existing patient.
     *
     * @param patient patient to be updated
     * @return the updated and persisted patient entity
     * @throws IllegalArgumentException if the id is null
     */
    @Override
    public Patient updatePatient(Patient patient) {
        // Ensure the ID is present for update operation
        Assert.notNull(patient.getId(),
                "L'ID est requis pour la mise à jour");

        return patientRepository.save(patient);
    }

}
