package com.hcltech.doctor_patient_service.controller;

import com.hcltech.doctor_patient_service.dto.DoctorDto;
import com.hcltech.doctor_patient_service.dto.PatientDto;
import com.hcltech.doctor_patient_service.service.DoctorService;
import com.hcltech.doctor_patient_service.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorControllerTest {

    @Mock
    private DoctorService doctorService;

    @Mock
    private PatientService patientService;

    @InjectMocks
    private DoctorController doctorController;

    private DoctorDto doctorDto;
    private PatientDto patientDto;

    @BeforeEach
    void setUp() {
        doctorDto = new DoctorDto();
        doctorDto.setId(1L);
        doctorDto.setName("Dr. Smith");

        patientDto = new PatientDto();
        patientDto.setId(1L);
        patientDto.setName("John Doe");
    }

    @Test
    @DisplayName("Test_CreateDoctor")
    void testCreateDoctor() {
        when(doctorService.create(doctorDto)).thenReturn(doctorDto);

        ResponseEntity<DoctorDto> response = doctorController.create(doctorDto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(doctorDto, response.getBody());
        verify(doctorService, times(1)).create(doctorDto);
    }

    @Test
    @DisplayName("Test_GetDoctorById")
    void testGetDoctorById() {
        when(doctorService.getById(1L)).thenReturn(doctorDto);

        ResponseEntity<DoctorDto> response = doctorController.getById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(doctorDto, response.getBody());
        verify(doctorService, times(1)).getById(1L);
    }

    @Test
    @DisplayName("Test_GetAllDoctors")
    void testGetAllDoctors() {
        List<DoctorDto> doctors = Collections.singletonList(doctorDto);
        when(doctorService.get()).thenReturn(doctors);

        ResponseEntity<List<DoctorDto>> response = doctorController.get();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(doctors, response.getBody());
        verify(doctorService, times(1)).get();
    }

    @Test
    @DisplayName("Test_GetPatientsByDoctorId")
    void testGetPatientsByDoctorId() {
        List<PatientDto> patients = Collections.singletonList(patientDto);
        when(patientService.getByDoctorId(1L)).thenReturn(patients);

        ResponseEntity<List<PatientDto>> response = doctorController.getByDoctorId(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(patients, response.getBody());
        verify(patientService, times(1)).getByDoctorId(1L);
    }

    @Test
    @DisplayName("Test_AssignPatient")
    void testAssignPatient() {
        // Mock the method to return true
        when(doctorService.assignPatient(1L, 1L)).thenReturn(true);

        // Call the method under test
        ResponseEntity<String> response = doctorController.assignPatient(1L, 1L);

        // Verify the response
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Patient assigned successfully", response.getBody());

        // Verify the interaction with the mock
        verify(doctorService, times(1)).assignPatient(1L, 1L);
    }

    @Test
    @DisplayName("Test_UnassignPatient")
    void testUnassignPatient() {
        doNothing().when(doctorService).unassignPatient(1L, 1L);

        ResponseEntity<String> response = doctorController.unassignPatient(1L, 1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Patient unassigned successfully", response.getBody());
        verify(doctorService, times(1)).unassignPatient(1L, 1L);
    }

    @Test
    @DisplayName("Test_DeleteDoctor")
    void testDeleteDoctor() {
        when(doctorService.delete(1L)).thenReturn(true);

        ResponseEntity<Void> response = doctorController.delete(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(doctorService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("Test_UpdateDoctor")
    void testUpdateDoctor() {
        when(doctorService.update(doctorDto)).thenReturn(Optional.of(doctorDto));

        ResponseEntity<DoctorDto> response = doctorController.update(doctorDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(doctorDto, response.getBody());
        verify(doctorService, times(1)).update(doctorDto);
    }
}