package com.medilabosolutions.patientservice.service.contracts;

import com.medilabosolutions.patientservice.model.Patient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IPatientService {

    List<Patient> findAllPatients();

    Optional<Patient> findPatient(int id);

    Patient addPatient(Patient patient);

    Patient updatePatient(Patient patient);

}