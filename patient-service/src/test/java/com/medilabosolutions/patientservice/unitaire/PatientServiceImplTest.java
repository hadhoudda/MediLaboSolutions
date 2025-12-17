package com.medilabosolutions.patientservice.unitaire;

import com.medilabosolutions.patientservice.model.Patient;
import com.medilabosolutions.patientservice.repository.PatientRepository;
import com.medilabosolutions.patientservice.service.PatientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PatientServiceImpl}.
 *
 * These tests verify the core service logic using mocked repository interactions.
 */
class PatientServiceImplTest {

    private PatientRepository patientRepository;
    private PatientServiceImpl patientService;

    /**
     * Initialize mocks and the service before each test.
     */
    @BeforeEach
    void setUp() {
        patientRepository = mock(PatientRepository.class);
        patientService = new PatientServiceImpl(patientRepository);
    }

    /**
     * Test that findAllPatients returns all patients from the repository.
     */
    @Test
    void testFindAllPatients() {
        final Patient patient1 = new Patient(1, "John", "Doe", LocalDate.of(1990, 1, 1), "M", "Address1", "123456");
        final Patient patient2 = new Patient(2, "Jane", "Doe", LocalDate.of(1995, 5, 5), "F", "Address2", "654321");

        when(patientRepository.findAll()).thenReturn(Arrays.asList(patient1, patient2));

        final List<Patient> patients = patientService.findAllPatients();

        assertEquals(2, patients.size());
        verify(patientRepository, times(1)).findAll();
    }

    /**
     * Test that findPatient returns a patient by ID when it exists.
     */
    @Test
    void testFindPatient() {
        final Patient patient = new Patient(1, "John", "Doe", LocalDate.of(1990, 1, 1), "M", "Address1", "123456");

        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));

        Optional<Patient> patientOpt = patientService.findPatient(1);

        assertTrue(patientOpt.isPresent());
        assertEquals("John", patientOpt.get().getFirstName());
        verify(patientRepository, times(1)).findById(1);
    }

    /**
     * Test that addPatient correctly saves a new patient and assigns an ID.
     */
    @Test
    void testAddPatient() {
        final Patient newPatient = new Patient(null, "John", "Doe", LocalDate.of(1990, 1, 1), "M", "Address1", "123456");

        // Mock repository to simulate ID generation
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> {
            Patient patient = invocation.getArgument(0);
            patient.setId(1);
            return patient;
        });

        final Patient result = patientService.addPatient(newPatient);

        assertNotNull(result.getId());
        assertEquals(1, result.getId());
        verify(patientRepository, times(1)).save(newPatient);
    }

    /**
     * Test that adding a patient with an existing ID throws IllegalArgumentException.
     */
    @Test
    void testAddPatientWithIdShouldThrow() {
        Patient patient = new Patient(10, "John", "Doe", LocalDate.of(1990, 1, 1), "M", "Address1", "123456");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> patientService.addPatient(patient)
        );

        assertTrue(ex.getMessage().contains("Le patient ne doit pas avoir d'ID"));
        verify(patientRepository, never()).save(any());
    }

    /**
     * Test that updatePatient saves an existing patient with a valid ID.
     */
    @Test
    void testUpdatePatient() {
        Patient p = new Patient(1, "John", "Doe", LocalDate.of(1990, 1, 1), "M", "Address1", "123456");

        when(patientRepository.save(p)).thenReturn(p);

        Patient result = patientService.updatePatient(p);

        assertEquals(1, result.getId());
        verify(patientRepository, times(1)).save(p);
    }

    /**
     * Test that updating a patient without an ID throws IllegalArgumentException.
     */
    @Test
    void testUpdatePatientWithoutIdShouldThrow() {
        Patient p = new Patient(null, "John", "Doe", LocalDate.of(1990, 1, 1), "M", "Address1", "123456");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> patientService.updatePatient(p)
        );

        assertTrue(ex.getMessage().contains("L'ID est requis pour la mise à jour"));
        verify(patientRepository, never()).save(any());
    }

}
