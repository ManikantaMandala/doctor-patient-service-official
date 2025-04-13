package com.hcltech.doctor_patient_service.service;

import com.hcltech.doctor_patient_service.dto.AuthenticationRequestDto;
import com.hcltech.doctor_patient_service.dto.AuthenticationResponseDto;
import com.hcltech.doctor_patient_service.entity.User;
import com.hcltech.doctor_patient_service.repository.UserRepository;
import com.hcltech.doctor_patient_service.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private JpaUserDetailsService jpaUserDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_UserDoesNotExist() {
        // Arrange
        AuthenticationRequestDto requestDto = new AuthenticationRequestDto();
        requestDto.setUsername("newUser");
        requestDto.setPassword("password");
        requestDto.setRoles("ROLE_USER");

        User user = new User();
        user.setUsername("newUser");
        user.setPassword("encodedPassword");
        user.setRoles("ROLE_USER");

        when(userRepository.existsByUsername("newUser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        AuthenticationRequestDto result = authenticationService.register(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals("newUser", result.getUsername());
        assertEquals("ROLE_USER", result.getRoles());
        verify(userRepository, times(1)).existsByUsername("newUser");
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_UserAlreadyExists() {
        // Arrange
        AuthenticationRequestDto requestDto = new AuthenticationRequestDto();
        requestDto.setUsername("existingUser");

        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authenticationService.register(requestDto);
        });
        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername("existingUser");
    }

    @Test
    void login_ValidCredentials() {
        // Arrange
        AuthenticationRequestDto requestDto = new AuthenticationRequestDto();
        requestDto.setUsername("validUser");
        requestDto.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        String jwt = "jwtToken";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jpaUserDetailsService.loadUserByUsername("validUser")).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn(jwt);

        // Act
        AuthenticationResponseDto responseDto = authenticationService.login(requestDto);

        // Assert
        assertNotNull(responseDto);
        assertEquals(jwt, responseDto.getJwt());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jpaUserDetailsService, times(1)).loadUserByUsername("validUser");
        verify(jwtUtil, times(1)).generateToken(userDetails);
    }

    @Test
    void login_InvalidCredentials() {
        // Arrange
        AuthenticationRequestDto requestDto = new AuthenticationRequestDto();
        requestDto.setUsername("invalidUser");
        requestDto.setPassword("wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new UsernameNotFoundException("Invalid UserName and password"));

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            authenticationService.login(requestDto);
        });
        assertEquals("Invalid UserName and password", exception.getMessage());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void toUser() {
        // Arrange
        AuthenticationRequestDto requestDto = new AuthenticationRequestDto();
        requestDto.setUsername("testUser");
        requestDto.setPassword("password");
        requestDto.setRoles("ROLE_USER");

        // Act
        User user = authenticationService.toUser(requestDto);

        // Assert
        assertNotNull(user);
        assertEquals("testUser", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("ROLE_USER", user.getRoles());
    }

    @Test
    void toAuthenticationRequestDto() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        user.setRoles("ROLE_USER");

        // Act
        AuthenticationRequestDto requestDto = authenticationService.toAuthenticationRequestDto(user);

        // Assert
        assertNotNull(requestDto);
        assertEquals("testUser", requestDto.getUsername());
        assertEquals("ROLE_USER", requestDto.getRoles());
    }
}