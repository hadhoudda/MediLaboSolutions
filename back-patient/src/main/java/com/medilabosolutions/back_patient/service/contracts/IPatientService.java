package com.medilabosolutions.back_patient.service.contracts;

import com.medilabosolutions.back_patient.model.Patient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface IPatientService {

    List<Patient> findAllPatients();
    Optional<Patient> findPatient(int id);
    Patient addPatient(Patient patient);
    Patient updatePatient(Patient patient);
    void deletePatient(int id);

}
