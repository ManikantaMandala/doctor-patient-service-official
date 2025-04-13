package com.hcltech.doctor_patient_service.service;

import com.hcltech.doctor_patient_service.dao.service.DoctorDaoService;
import com.hcltech.doctor_patient_service.dao.service.PatientDaoService;
import com.hcltech.doctor_patient_service.dto.DoctorDto;
import com.hcltech.doctor_patient_service.entity.Doctor;
import com.hcltech.doctor_patient_service.entity.Patient;
import com.hcltech.doctor_patient_service.exception.DoctorNotFoundException;
import com.hcltech.doctor_patient_service.exception.DoctorPatientLimitExceededException;
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
class DoctorServiceTest {

    @Mock
    private DoctorDaoService doctorDaoService;

    @Mock
    private PatientDaoService patientDaoService;

    @InjectMocks
    private DoctorService doctorService;

    private Doctor doctor;
    private DoctorDto doctorDto;
    private Patient patient;

    @BeforeEach
    void setUp() {
        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Dr. Smith");
        doctor.setPatients(new ArrayList<>());
        doctorDto = new DoctorDto();
        doctorDto.setId(1L);
        doctorDto.setName("Dr. Smith");

        patient = new Patient();
        patient.setId(1L);
        patient.setName("John Doe");
    }

    @Test
    @DisplayName("Test_CreateDoctor")
    void testCreate() {
        when(doctorDaoService.create(any(Doctor.class))).thenReturn(doctor);

        DoctorDto createdDoctorDto = doctorService.create(doctorDto);

        assertNotNull(createdDoctorDto);
        assertEquals(doctorDto.getName(), createdDoctorDto.getName());
        assertEquals(doctorDto.getId(), createdDoctorDto.getId());
        verify(doctorDaoService, times(1)).create(any(Doctor.class));
    }

    @Test
    @DisplayName("Test_CreateDoctor_ExceedsPatientLimit")
    void testCreateDoctorExceedsPatientLimit() {
        doctorDto.setPatientIds(List.of(1L, 2L, 3L, 4L,5L));
        when(patientDaoService.getById(any())).thenReturn(Optional.ofNullable(patient));
        Exception exception = assertThrows(DoctorPatientLimitExceededException.class, () ->
                doctorService.create(doctorDto));

        assertEquals("Doctor has exceeded the maximum patient limit of 4", exception.getMessage());
    }

    @Test
    @DisplayName("Test_GetDoctorById")
    void testGetById() {
        when(doctorDaoService.getById(1L)).thenReturn(Optional.of(doctor));

        DoctorDto foundDoctorDto = doctorService.getById(1L);

        assertNotNull(foundDoctorDto);
        assertEquals("Dr. Smith", foundDoctorDto.getName());
        verify(doctorDaoService, times(1)).getById(1L);
    }

    @Test
    @DisplayName("Test_GetDoctorById_NotFound")
    void testGetDoctorByIdNotFound() {
        when(doctorDaoService.getById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(DoctorNotFoundException.class, () ->
                doctorService.getById(1L));

        assertEquals("Doctor not found for Id:1", exception.getMessage());
    }

    @Test
    @DisplayName("Test_GetAllDoctors")
    void testGet() {
        List<Doctor> doctors = Collections.singletonList(doctor);
        when(doctorDaoService.get()).thenReturn(doctors);

        List<DoctorDto> foundDoctors = doctorService.get();

        assertEquals(1, foundDoctors.size());
        verify(doctorDaoService, times(1)).get();
    }

    @Test
    @DisplayName("Test_AssignPatient")
    void testAssignPatient() {
        when(doctorDaoService.assignPatient(1L, 1L)).thenReturn(true);

        boolean result = doctorService.assignPatient(1L, 1L);

        assertTrue(result);
        verify(doctorDaoService, times(1)).assignPatient(1L, 1L);
    }

    @Test
    @DisplayName("Test_UnassignPatient")
    void testUnassignPatient() {
        doNothing().when(doctorDaoService).unassignPatient(1L, 1L);

        doctorService.unassignPatient(1L, 1L);

        verify(doctorDaoService, times(1)).unassignPatient(1L, 1L);
    }

    @Test
    @DisplayName("Test_DeleteDoctor")
    void testDelete() {
        doNothing().when(doctorDaoService).delete(1L);

        boolean result = doctorService.delete(1L);

        assertTrue(result);
        verify(doctorDaoService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("Test_UpdateDoctor")
    void testUpdate() {
        when(doctorDaoService.createDoctor(any(Doctor.class))).thenReturn(Optional.of(doctor));

        Optional<DoctorDto> updatedDoctorDto = doctorService.update(doctorDto);

        assertTrue(updatedDoctorDto.isPresent());
        assertEquals("Dr. Smith", updatedDoctorDto.get().getName());
        verify(doctorDaoService, times(1)).createDoctor(any(Doctor.class));
    }

    @Test
    @DisplayName("Test_UpdateDoctor_ExceedsPatientLimit")
    void testUpdateDoctorExceedsPatientLimit() {
        doctorDto.setPatientIds(List.of(1L, 2L, 3L, 4L, 5L));

        Exception exception = assertThrows(RuntimeException.class, () ->
                doctorService.update(doctorDto));

        assertEquals("Doctor has exceeded the maximum patient limit of 4", exception.getMessage());
    }

    @Test
    void noOfPatients_DoctorExists() {
        // Arrange
        Long doctorId = 1L;
        Doctor doctors = new Doctor();
        doctors.setPatients(Collections.singletonList(new Patient()));
        when(doctorDaoService.getById(doctorId)).thenReturn(Optional.of(doctors));

        // Act
        int result = doctorService.noOfPatients(doctorId);

        // Assert
        assertEquals(1, result);
        verify(doctorDaoService, times(1)).getById(doctorId);
    }

    @Test
    void noOfPatients_DoctorDoesNotExist() {
        // Arrange
        Long doctorId = 1L;
        when(doctorDaoService.getById(doctorId)).thenReturn(Optional.empty());

        // Act
        int result = doctorService.noOfPatients(doctorId);

        // Assert
        assertEquals(0, result);
        verify(doctorDaoService, times(1)).getById(doctorId);
    }
}