package com.hcltech.doctor_patient_service.exception;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.converter.HttpMessageNotReadableException;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorPatientServiceExceptionHandlerTest {

    @InjectMocks
    private DoctorPatientServiceExceptionHandler exceptionHandler;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(request.getRequestURI()).thenReturn("/test-endpoint");
    }

    @Test
    void handleDoctorNotFoundException() {
        DoctorNotFoundException exception = new DoctorNotFoundException("Doctor not found");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleDoctorNotFoundException(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Doctor not found", response.getBody().getMessage());
        assertEquals("/test-endpoint", response.getBody().getPath());
    }

    @Test
    void handlePatientNotFoundException() {
        PatientNotFoundException exception = new PatientNotFoundException("Patient not found");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handlePatientNotFoundException(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Patient not found", response.getBody().getMessage());
    }

    @Test
    void handleDoctorPatientLimitExceededException() {
        DoctorPatientLimitExceededException exception = new DoctorPatientLimitExceededException("Limit exceeded");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleDoctorPatientLimitExceededException(exception, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Limit exceeded", response.getBody().getMessage());
    }

    @Test
    void handleInvalidLoginCredentialException() {
        InvalidLoginCredentialException exception = new InvalidLoginCredentialException("Invalid credentials");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleInvalidLoginCredentialException(exception, request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody().getMessage());
    }

    @Test
    void handleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleIllegalArgumentException(exception, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Invalid argument", response.getBody().getMessage());
    }


    @Test
    void handleHttpMessageNotReadableException() {
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Malformed JSON request");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleHttpMessageNotReadableException(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Malformed JSON request", response.getBody().getMessage());
    }





    @Test
    void handlePatientNotAssignedException() {
        PatientNotAssignedException exception = new PatientNotAssignedException("Patient not assigned");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handlePatientNotAssignedException(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Patient not assigned", response.getBody().getMessage());
    }




}