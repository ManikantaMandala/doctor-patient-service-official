package com.hcltech.doctor_patient_service.dao.service;
import com.hcltech.doctor_patient_service.entity.Patient;
import com.hcltech.doctor_patient_service.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientDaoServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientDaoService patientDaoService;

    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);
        patient.setName("John Doe");
    }

    @Test
    @DisplayName("Test_Create")
    void testCreate() {
        when(patientRepository.save(patient)).thenReturn(patient);

        Patient createdPatient = patientDaoService.create(patient);

        assertNotNull(createdPatient);
        assertEquals("John Doe", createdPatient.getName());
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    @DisplayName("Test_GetById")
    void testGetById() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        Optional<Patient> foundPatient = patientDaoService.getById(1L);

        assertTrue(foundPatient.isPresent());
        assertEquals("John Doe", foundPatient.get().getName());
        verify(patientRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test_GetAllById")
    void testGetAllById() {
        List<Patient> patients = Arrays.asList(patient);
        when(patientRepository.findAllById(Arrays.asList(1L, 2L))).thenReturn(patients);

        List<Patient> foundPatients = patientDaoService.getAllById(Arrays.asList(1L, 2L));

        assertEquals(1, foundPatients.size());
        verify(patientRepository, times(1)).findAllById(Arrays.asList(1L, 2L));
    }

    @Test
    @DisplayName("Test_GetAllById_NullIds")
    void testGetAllByIdNullIds() {
        List<Patient> foundPatients = patientDaoService.getAllById(null);

        assertTrue(foundPatients.isEmpty());
        verify(patientRepository, never()).findAllById(any());
    }

    @Test
    @DisplayName("Test_GetByDoctorId")
    void testGetByDoctorId() {
        List<Patient> patients = Collections.singletonList(patient);
        when(patientRepository.findByDoctorId(1L)).thenReturn(patients);

        List<Patient> foundPatients = patientDaoService.getByDoctorId(1L);

        assertEquals(1, foundPatients.size());
        verify(patientRepository, times(1)).findByDoctorId(1L);
    }

    @Test
    @DisplayName("Test_GetAll")
    void testGetAll() {
        List<Patient> patients = Collections.singletonList(patient);
        when(patientRepository.findAll()).thenReturn(patients);

        List<Patient> foundPatients = patientDaoService.get();

        assertEquals(1, foundPatients.size());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test_Delete")
    void testDelete() {
        doNothing().when(patientRepository).deleteById(1L);

        patientDaoService.delete(1L);

        verify(patientRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Test_Update")
    void testUpdate() {
        when(patientRepository.save(patient)).thenReturn(patient);

        Patient updatedPatient = patientDaoService.update(patient);

        assertNotNull(updatedPatient);
        assertEquals("John Doe", updatedPatient.getName());
        verify(patientRepository, times(1)).save(patient);
    }
}
