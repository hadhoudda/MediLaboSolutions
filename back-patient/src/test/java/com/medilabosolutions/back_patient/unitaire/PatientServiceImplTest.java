package com.medilabosolutions.back_patient.unitaire;


import com.medilabosolutions.back_patient.model.Patient;
import com.medilabosolutions.back_patient.repository.PatientRepository;
import com.medilabosolutions.back_patient.service.PatientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceImplTest {

    private PatientRepository patientRepository;
    private PatientServiceImpl patientService;

    @BeforeEach
    void setUp() {
        patientRepository = mock(PatientRepository.class);
        patientService = new PatientServiceImpl(patientRepository);
    }

    @Test
    void testFindAllPatients() {
        Patient p1 = new Patient(1, "John", "Doe", LocalDate.of(1990,1,1), "M", "Address1", "123456");
        Patient p2 = new Patient(2, "Jane", "Doe", LocalDate.of(1995,5,5), "F", "Address2", "654321");
        when(patientRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Patient> patients = patientService.findAllPatients();

        assertEquals(2, patients.size());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void testFindPatient() {
        Patient p = new Patient(1, "John", "Doe", LocalDate.of(1990,1,1), "M", "Address1", "123456");
        when(patientRepository.findById(1)).thenReturn(Optional.of(p));

        Optional<Patient> patientOpt = patientService.findPatient(1);

        assertTrue(patientOpt.isPresent());
        assertEquals("John", patientOpt.get().getFirstName());
        verify(patientRepository, times(1)).findById(1);
    }

    @Test
    void testAddPatient() {
        Patient p = new Patient(null, "John", "Doe", LocalDate.of(1990,1,1), "M", "Address1", "123456");
        Patient saved = new Patient(1, "John", "Doe", LocalDate.of(1990,1,1), "M", "Address1", "123456");
        when(patientRepository.save(p)).thenReturn(saved);

        Patient result = patientService.addPatient(p);

        assertNotNull(result.getId());
        assertEquals(1, result.getId());
        verify(patientRepository, times(1)).save(p);
    }

    @Test
    void testAddPatientWithIdShouldThrow() {
        Patient p = new Patient(10, "John", "Doe", LocalDate.of(1990,1,1), "M", "Address1", "123456");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> patientService.addPatient(p));
        assertTrue(ex.getMessage().contains("Le patient ne doit pas avoir d'ID"));
        verify(patientRepository, never()).save(any());
    }

    @Test
    void testUpdatePatient() {
        Patient p = new Patient(1, "John", "Doe", LocalDate.of(1990,1,1), "M", "Address1", "123456");
        when(patientRepository.save(p)).thenReturn(p);

        Patient result = patientService.updatePatient(p);

        assertEquals(1, result.getId());
        verify(patientRepository, times(1)).save(p);
    }

    @Test
    void testUpdatePatientWithoutIdShouldThrow() {
        Patient p = new Patient(null, "John", "Doe", LocalDate.of(1990,1,1), "M", "Address1", "123456");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> patientService.updatePatient(p));
        assertTrue(ex.getMessage().contains("L'ID est requis pour la mise à jour"));
        verify(patientRepository, never()).save(any());
    }
}
