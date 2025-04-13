package com.hcltech.doctor_patient_service.dto;

import java.util.List;

public class AdvancedHealthcareDto {
    private Long id;
    private boolean heartDisease;
    private boolean diabetes;
    private boolean hypertension;
    private List<ChronicConditionDto> chronicConditionDto;
    private List<AllergyDto> allergyDto;
    private List<MedicationDto> medicationDto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isHeartDisease() {
        return heartDisease;
    }

    public void setHeartDisease(boolean heartDisease) {
        this.heartDisease = heartDisease;
    }

    public boolean isDiabetes() {
        return diabetes;
    }

    public void setDiabetes(boolean diabetes) {
        this.diabetes = diabetes;
    }

    public boolean isHypertension() {
        return hypertension;
    }

    public void setHypertension(boolean hypertension) {
        this.hypertension = hypertension;
    }

    public List<ChronicConditionDto> getChronicConditionDto() {
        return chronicConditionDto;
    }

    public void setChronicConditionDto(List<ChronicConditionDto> chronicConditionDto) {
        this.chronicConditionDto = chronicConditionDto;
    }

    public List<AllergyDto> getAllergyDto() {
        return allergyDto;
    }

    public void setAllergyDto(List<AllergyDto> allergyDto) {
        this.allergyDto = allergyDto;
    }

    public List<MedicationDto> getMedicationDto() {
        return medicationDto;
    }

    public void setMedicationDto(List<MedicationDto> medicationDto) {
        this.medicationDto = medicationDto;
    }
}
