package com.medilabosolutions.patientservice.repository;

import com.medilabosolutions.patientservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
}
