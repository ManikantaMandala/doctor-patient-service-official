package com.hcltech.doctor_patient_service.dao.service;

import com.hcltech.doctor_patient_service.entity.Doctor;
import com.hcltech.doctor_patient_service.entity.Patient;
import com.hcltech.doctor_patient_service.exception.DoctorNotFoundException;
import com.hcltech.doctor_patient_service.exception.PatientNotAssignedException;
import com.hcltech.doctor_patient_service.exception.PatientNotFoundException;
import com.hcltech.doctor_patient_service.repository.DoctorRepository;
import com.hcltech.doctor_patient_service.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorDaoServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private DoctorDaoService doctorDaoService;

    private Doctor doctor;
    private Patient patient;

    @BeforeEach
    void setUp() {
        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Dr. Smith");

        patient = new Patient();
        patient.setId(1L);
        patient.setName("John Doe");
    }

    @Test
    @DisplayName("Test_CreateDoctor")
    void testCreateDoctor() {
        when(doctorRepository.save(doctor)).thenReturn(doctor);

        Doctor createdDoctor = doctorDaoService.create(doctor);

        assertNotNull(createdDoctor);
        assertEquals("Dr. Smith", createdDoctor.getName());
        verify(doctorRepository, times(1)).save(doctor);
    }

    @Test
    @DisplayName("Test_GetDoctorById")
    void testGetDoctorById() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        Optional<Doctor> foundDoctor = doctorDaoService.getById(1L);

        assertTrue(foundDoctor.isPresent());
        assertEquals("Dr. Smith", foundDoctor.get().getName());
        verify(doctorRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test_GetAllDoctors")
    void testGetAllDoctors() {
        List<Doctor> doctors = Collections.singletonList(doctor);
        when(doctorRepository.findAll()).thenReturn(doctors);

        List<Doctor> foundDoctors = doctorDaoService.get();

        assertEquals(1, foundDoctors.size());
        verify(doctorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test_AssignPatient")
    void testAssignPatient() {
        doctor.setPatients(new ArrayList<>());
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        boolean result = doctorDaoService.assignPatient(1L, 1L);

        assertTrue(result);
        assertEquals(doctor, patient.getDoctor());
        verify(patientRepository,times(1)).save(patient);
    }

    @Test
    @DisplayName("Test_AssignPatient_DoctorNotFound")
    void testAssignPatientDoctorNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(new Patient()));
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(DoctorNotFoundException.class, () ->
                doctorDaoService.assignPatient(1L, 1L));

        assertEquals("Doctor not found", exception.getMessage());
    }

    @Test
    @DisplayName("Test_AssignPatient_PatientNotFound")
    void testAssignPatientPatientNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(PatientNotFoundException.class, () ->
                doctorDaoService.assignPatient(1L, 1L));

        assertEquals("Patient not found", exception.getMessage());
    }

    @Test
    @DisplayName("Test_UnassignPatient")
    void testUnassignPatient() {
        doctor.setPatients(new ArrayList<>(List.of(patient)));
        patient.setDoctor(doctor);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        doctorDaoService.unassignPatient(1L, 1L);
        assertNull(patient.getDoctor(),"Patient should not have a doctor after unassignment");
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    @DisplayName("Test_Unassign_Patient_PatientNotAssigned")
    void testUnassignPatientPatientNotAssigned() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        Exception exception = assertThrows(PatientNotAssignedException.class, () ->
                doctorDaoService.unassignPatient(1L, 1L));

        assertEquals("patient is not assigned to this doctor", exception.getMessage());
    }

    @Test
    @DisplayName("Test_Delete_Doctor")
    void testDeleteDoctor() {
        doNothing().when(doctorRepository).deleteById(1L);

        doctorDaoService.delete(1L);

        verify(doctorRepository, times(1)).deleteById(1L);
    }
}
