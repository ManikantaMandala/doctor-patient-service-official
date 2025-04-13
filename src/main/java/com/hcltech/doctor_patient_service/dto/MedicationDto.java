package com.hcltech.doctor_patient_service.dto;

import jakarta.validation.constraints.NotBlank;

public class MedicationDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String dosage;

    public MedicationDto(Long id, String name, String dosage) {
        this.id = id;
        this.name = name;
        this.dosage = dosage;
    }

    public MedicationDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public @NotBlank String getDosage() {
        return dosage;
    }

    public void setDosage(@NotBlank String dosage) {
        this.dosage = dosage;
    }
}