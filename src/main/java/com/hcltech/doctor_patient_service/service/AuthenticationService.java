package com.hcltech.doctor_patient_service.service;

import com.hcltech.doctor_patient_service.dto.AuthenticationRequestDto;
import com.hcltech.doctor_patient_service.dto.AuthenticationResponseDto;
import com.hcltech.doctor_patient_service.entity.User;
import com.hcltech.doctor_patient_service.repository.UserRepository;
import com.hcltech.doctor_patient_service.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final JpaUserDetailsService jpaUserDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            JpaUserDetailsService jpaUserDetailsService,
            JwtUtil jwtUtil,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.jpaUserDetailsService = jpaUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthenticationRequestDto register(final AuthenticationRequestDto authenticationRequestDto) {
        logger.info("Registering user: {}", authenticationRequestDto.getUsername());
        if (userRepository.existsByUsername(authenticationRequestDto.getUsername())) {
            logger.warn("Username already exists: {}", authenticationRequestDto.getUsername());
            throw new IllegalArgumentException("Username already exists");
        }
        final User user = toUser(authenticationRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        final User result = userRepository.save(user);
        logger.info("User registered successfully: {}", result.getUsername());
        return toAuthenticationRequestDto(result);
    }

    public AuthenticationResponseDto login(AuthenticationRequestDto authenticationRequestDto) {
        logger.info("Logging in user: {}", authenticationRequestDto.getUsername());
        try {
            final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequestDto.getUsername(), authenticationRequestDto.getPassword()));
            if (authentication.isAuthenticated()) {
                final UserDetails userDetails = jpaUserDetailsService.loadUserByUsername(authenticationRequestDto.getUsername());
                final String jwt = jwtUtil.generateToken(userDetails);
                logger.info("User logged in successfully: {}", authenticationRequestDto.getUsername());
                return new AuthenticationResponseDto(jwt);
            }
        } catch (Exception e) {
            logger.error("Authentication failed for user: {}", authenticationRequestDto.getUsername(), e);
            throw new UsernameNotFoundException("Invalid UserName and password");
        }
        logger.error("Authentication failed for user: {}", authenticationRequestDto.getUsername());
        throw new UsernameNotFoundException(authenticationRequestDto.getUsername() + " not found");
    }

    public User toUser(AuthenticationRequestDto authenticationRequestDto) {
        return new User(authenticationRequestDto.getUsername(), authenticationRequestDto.getPassword(), authenticationRequestDto.getRoles());
    }

    public AuthenticationRequestDto toAuthenticationRequestDto(User user) {
        final AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto();
        authenticationRequestDto.setUsername(user.getUsername());
        authenticationRequestDto.setRoles(user.getRoles());
        return authenticationRequestDto;
    }

}

