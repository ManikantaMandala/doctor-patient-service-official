package com.hcltech.doctor_patient_service.dto;

import jakarta.validation.constraints.NotBlank;

public class AllergyDto {
    private Long id;
    @NotBlank
    private String types;

    // Constructor to initialize fields
    public AllergyDto(Long id, String types) {
        this.id = id;
        this.types = types;
    }

    // Default constructor
    public AllergyDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank String getTypes() {
        return types;
    }

    public void setTypes(@NotBlank String types) {
        this.types = types;
    }
}