package com.hcltech.doctor_patient_service.dto;

import jakarta.validation.constraints.NotBlank;

public class ChronicConditionDto {
    private Long id;
    @NotBlank
    private String condition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank String getCondition() {
        return condition;
    }

    public void setCondition(@NotBlank String condition) {
        this.condition = condition;
    }
}