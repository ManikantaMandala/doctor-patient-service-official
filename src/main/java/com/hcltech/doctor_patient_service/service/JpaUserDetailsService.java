package com.hcltech.doctor_patient_service.service;

import com.hcltech.doctor_patient_service.entity.User;
import com.hcltech.doctor_patient_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(JpaUserDetailsService.class);
    private final UserRepository userRepository;

    @Autowired
    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        logger.info("Loading user by username: {}", username);

        final User user = userRepository.findByUsername(username).orElseThrow(() -> {
            logger.warn("User not found: {}", username);
            return new UsernameNotFoundException("User not found: " + username); // More descriptive message
        });
        logger.info("User found: {}", username);
        return toUserDetails(user);
    }

    public UserDetails toUserDetails(User user) {
        String[] rolesArray = (user.getRoles() != null && !user.getRoles().isEmpty()) ? user.getRoles().split(",") : new String[0];

        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(rolesArray)
                .build();
        logger.debug("User details built for user: {}", user.getUsername());
        return userDetails;
    }
}