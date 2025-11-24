package com.medilabosolutions.patientservice.unitaire;

import com.medilabosolutions.patientservice.model.Patient;
import com.medilabosolutions.patientservice.repository.PatientRepository;
import com.medilabosolutions.patientservice.service.PatientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
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

    // Utilitaire pour convertir LocalDate → Date
    private Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    Patient patientTest1 = new Patient(
            1,
            "John",
            "Doe",
            toDate(LocalDate.of(1990, 1, 1)),
            "M",
            "Address1",
            "123456"
    );

    Patient patientTest2 = new Patient(
            2,
            "Jane",
            "Doe",
            toDate(LocalDate.of(1995, 5, 5)),
            "F",
            "Address2",
            "654321"
    );


    @Test
    void testFindAllPatients() {
        Patient p1 = patientTest1;
        Patient p2 = patientTest2;

        when(patientRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Patient> patients = patientService.findAllPatients();

        assertEquals(2, patients.size());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void testFindPatient() {
        Patient p = patientTest1;

        when(patientRepository.findById(1)).thenReturn(Optional.of(p));

        Optional<Patient> patientOpt = patientService.findPatient(1);

        assertTrue(patientOpt.isPresent());
        assertEquals("John", patientOpt.get().getFirstName());
        verify(patientRepository, times(1)).findById(1);
    }

    @Test
    void testAddPatient() {
        Patient p = new Patient(
                null,
                "John",
                "Doe",
                toDate(LocalDate.of(1990, 1, 1)),
                "M",
                "Address1",
                "123456"
        );

        Patient saved = patientTest1;

        when(patientRepository.save(p)).thenReturn(saved);

        Patient result = patientService.addPatient(p);

        assertNotNull(result.getId());
        assertEquals(1, result.getId());
        verify(patientRepository, times(1)).save(p);
    }

    @Test
    void testAddPatientWithIdShouldThrow() {
        Patient p = new Patient(
                10,
                "John",
                "Doe",
                toDate(LocalDate.of(1990, 1, 1)),
                "M",
                "Address1",
                "123456"
        );

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> patientService.addPatient(p)
        );

        assertTrue(ex.getMessage().contains("Le patient ne doit pas avoir d'ID"));
        verify(patientRepository, never()).save(any());
    }

    @Test
    void testUpdatePatient() {
        Patient p = patientTest1;

        when(patientRepository.save(p)).thenReturn(p);

        Patient result = patientService.updatePatient(p);

        assertEquals(1, result.getId());
        verify(patientRepository, times(1)).save(p);
    }

    @Test
    void testUpdatePatientWithoutIdShouldThrow() {
        Patient p = new Patient(
                null,
                "John",
                "Doe",
                toDate(LocalDate.of(1990, 1, 1)),
                "M",
                "Address1",
                "123456"
        );

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> patientService.updatePatient(p)
        );

        assertTrue(ex.getMessage().contains("L'ID est requis pour la mise à jour"));
        verify(patientRepository, never()).save(any());
    }
}
