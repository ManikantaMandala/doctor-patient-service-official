package com.hcltech.doctor_patient_service.dto;

import com.hcltech.doctor_patient_service.enums.SpecialistType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;

public class DoctorDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String qualification;
  
    private SpecialistType specialist;

    private List<Long> patientIds;

    public DoctorDto() {

    }

    public DoctorDto(long l, String johnDoe, String mbbs, SpecialistType specialistType) {
        id=l;
        name=johnDoe;
        qualification=mbbs;
        specialist=specialistType;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorDto that = (DoctorDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(qualification, that.qualification) &&
                specialist == that.specialist ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, qualification, specialist);
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

    public @NotBlank String getQualification() {
        return qualification;
    }

    public void setQualification(@NotBlank String qualification) {
        this.qualification = qualification;
    }

    public @NotNull SpecialistType getSpecialist() {
        return specialist;
    }

    public void setSpecialist(@NotNull SpecialistType specialist) {
        this.specialist = specialist;
    }

    public List<Long> getPatientIds() {
        return patientIds;
    }

    public void setPatientIds(List<Long> patientIds) {
        this.patientIds = patientIds;
    }


}
