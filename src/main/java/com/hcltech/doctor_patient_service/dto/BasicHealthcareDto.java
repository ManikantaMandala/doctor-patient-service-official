package com.hcltech.doctor_patient_service.dto;

import com.hcltech.doctor_patient_service.enums.BloodGroup;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class BasicHealthcareDto {
    private Long id;
    @Min(0)
    private double height;
    @Min(0)
    private double weight;
    private String bloodPressure;
    private String sugar;
    @NotNull
    private BloodGroup bloodGroup;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Min(0)
    public double getHeight() {
        return height;
    }

    public void setHeight(@Min(0) double height) {
        this.height = height;
    }

    @Min(0)
    public double getWeight() {
        return weight;
    }

    public void setWeight(@Min(0) double weight) {
        this.weight = weight;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getSugar() {
        return sugar;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    public @NotNull BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(@NotNull BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
}
