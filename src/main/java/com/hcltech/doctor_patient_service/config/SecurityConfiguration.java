package com.hcltech.doctor_patient_service.config;

import com.hcltech.doctor_patient_service.filter.JwtAuthRequestFilter;
import com.hcltech.doctor_patient_service.service.JpaUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

    private static final String[] SWAGGER_WHITE_LIST        = {
            "/swagger-ui.html",
            "/swagger-ui/index.html",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**" };
    private static final String[] H2_CONSOLE_WHITE_LIST     = {
            "/h2-console/**"
    };
    private static final String[] AUTHENTICATION_WHITE_LIST = {
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/api/v1/auth/logout"
    };

    private final JwtAuthRequestFilter jwtAuthRequestFilter;
    private final JpaUserDetailsService jpaUserDetailsService;

    @Autowired
    public SecurityConfiguration(JwtAuthRequestFilter jwtAuthRequestFilter,
                                 JpaUserDetailsService jpaUserDetailsService) {
        this.jwtAuthRequestFilter = jwtAuthRequestFilter;
        this.jpaUserDetailsService = jpaUserDetailsService;
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring security filter chain...");
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .headers(header ->
                        header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        http.authorizeHttpRequests(requests ->
                requests.requestMatchers(SWAGGER_WHITE_LIST)
                .permitAll()
                .requestMatchers(H2_CONSOLE_WHITE_LIST)
                .permitAll()
                .requestMatchers(AUTHENTICATION_WHITE_LIST)
                .permitAll()
                .anyRequest()
                .authenticated());
        logger.info("Public endpoints configured: Swagger, H2 console, and authentication endpoints.");
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        logger.info("Session management set to stateless.");
        http.authenticationProvider(authenticationProvider(jpaUserDetailsService));
        logger.info("Authentication provider configured.");
        http.addFilterBefore(jwtAuthRequestFilter, UsernamePasswordAuthenticationFilter.class);
        logger.info("JWT authentication filter added.");
        http.httpBasic(withDefaults());
        logger.info("HTTP Basic authentication REMOVED.  JWT is used.");
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(JpaUserDetailsService jpaUserDetailsService) {
        logger.info("Setting up DaoAuthenticationProvider...");
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(jpaUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        logger.info("DaoAuthenticationProvider setup complete.");
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        logger.info("Initializing AuthenticationManager...");
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Using NoOpPasswordEncoder for password hashing.");
        return new BCryptPasswordEncoder();
    }
}
