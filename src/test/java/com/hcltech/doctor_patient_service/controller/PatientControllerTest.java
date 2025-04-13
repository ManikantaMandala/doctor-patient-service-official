
package com.hcltech.doctor_patient_service.controller;

import com.hcltech.doctor_patient_service.dto.PatientDto;
import com.hcltech.doctor_patient_service.service.PatientService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientControllerTest {

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    private PatientDto patientDto;
    void initializeTestEnvironment() {
        patientDto = new PatientDto();
        patientDto.setId(1L);
        patientDto.setName("John Doe");
    }

    @Test
    @DisplayName("Test_CreatePatient")
    void testCreatePatient() {
        when(patientService.create(patientDto)).thenReturn(patientDto);

        ResponseEntity<PatientDto> response = patientController.create(patientDto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(patientDto, response.getBody());
        verify(patientService, times(1)).create(patientDto);
    }

    @Test
    @DisplayName("Test_GetPatientById")
    void testGetPatientById() {
        when(patientService.getById(1L)).thenReturn(null);

        ResponseEntity<PatientDto> response = patientController.getById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(patientDto, response.getBody());
        verify(patientService, times(1)).getById(1L);
    }

    @Test
    @DisplayName("Test_GetAllPatients")
    void testGetAllPatients() {
        List<PatientDto> patients = Collections.singletonList(patientDto);
        when(patientService.get()).thenReturn(patients);

        ResponseEntity<List<PatientDto>> response = patientController.get();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(patients, response.getBody());
        verify(patientService, times(1)).get();
    }

    @Test
    @DisplayName("Test_UpdatePatient")
    void testUpdatePatient() {
        when(patientService.update(1L, patientDto)).thenReturn(patientDto);

        ResponseEntity<PatientDto> response = patientController.update(1L, patientDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(patientDto, response.getBody());
        verify(patientService, times(1)).update(1L, patientDto);
    }

    @Test
    @DisplayName("Test_DeletePatient")
    void testDeletePatient() {
        when(patientService.delete(1L)).thenReturn(true);

        ResponseEntity<Void> response = patientController.delete(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(patientService, times(1)).delete(1L);
    }

    @Test
     void testDeletePatient_NotFound() {
        when(patientService.delete(-1L)).thenReturn(false);

        ResponseEntity<Void> response = patientController.delete(-1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(patientService, times(1)).delete(-1L);
    }
}