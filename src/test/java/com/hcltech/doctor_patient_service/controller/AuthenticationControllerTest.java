package com.hcltech.doctor_patient_service.controller;

import com.hcltech.doctor_patient_service.dto.AuthenticationRequestDto;
import com.hcltech.doctor_patient_service.dto.AuthenticationResponseDto;
import com.hcltech.doctor_patient_service.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_Success() {
        // Arrange
        AuthenticationRequestDto requestDto = new AuthenticationRequestDto();
        requestDto.setUsername("newUser");
        requestDto.setPassword("password");
        requestDto.setRoles("ROLE_USER");

        AuthenticationRequestDto registeredUser = new AuthenticationRequestDto();
        registeredUser.setUsername("newUser");
        registeredUser.setRoles("ROLE_USER");

        when(authenticationService.register(requestDto)).thenReturn(registeredUser);

        // Act
        ResponseEntity<AuthenticationRequestDto> response = authenticationController.register(requestDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(registeredUser, response.getBody());
        verify(authenticationService, times(1)).register(requestDto);
    }

    @Test
    void login_Success() {
        // Arrange
        AuthenticationRequestDto requestDto = new AuthenticationRequestDto();
        requestDto.setUsername("validUser");
        requestDto.setPassword("password");

        AuthenticationResponseDto responseDto = new AuthenticationResponseDto("jwtToken");

        when(authenticationService.login(requestDto)).thenReturn(responseDto);

        // Act
        ResponseEntity<AuthenticationResponseDto> response = authenticationController.login(requestDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(authenticationService, times(1)).login(requestDto);
    }

    @Test
    void logout() {
        // Arrange
        AuthenticationRequestDto requestDto = new AuthenticationRequestDto();
        requestDto.setUsername("user");
        requestDto.setPassword("password");

        // Act
        ResponseEntity<AuthenticationResponseDto> response = authenticationController.logout(requestDto);

        // Assert
        assertNull(response);
    }
}