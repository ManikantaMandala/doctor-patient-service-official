package com.hcltech.doctor_patient_service.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    private static final Logger logger = LoggerFactory.getLogger(OpenAPIConfig.class);

    private static final String SCHEME_NAME = "Authorization";
    private static final String SCHEME = "bearer";
    private static final String BEARER_FORMAT = "JWT";

    @Bean    public OpenAPI openAPI() {
        logger.info("Initializing OpenAPI configuration...");
        OpenAPI openAPI = new OpenAPI().components(components())
                .addSecurityItem(securityRequirement());
        logger.info("OpenAPI configuration initialized successfully.");
        return openAPI;
    }

    private Components components() {
        logger.info("Configuring OpenAPI security components...");
        Components components = new Components().addSecuritySchemes(SCHEME_NAME, securityScheme());
        logger.info("OpenAPI security components configured successfully.");
        return components;
    }

    private SecurityScheme securityScheme() {
        logger.info("Setting up SecurityScheme with scheme: {}, bearer format: {}, location: {}", SCHEME, BEARER_FORMAT, In.HEADER);
        SecurityScheme securityScheme = new SecurityScheme().name(SCHEME_NAME)
                .type(Type.APIKEY)
                .in(In.HEADER)
                .scheme(SCHEME)
                .bearerFormat(BEARER_FORMAT);
        logger.info("SecurityScheme setup completed.");
        return securityScheme;
    }

    private SecurityRequirement securityRequirement() {
        logger.info("Defining SecurityRequirement for the OpenAPI...");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(SCHEME_NAME);
        logger.info("SecurityRequirement defined successfully.");
        return securityRequirement;
    }
}
