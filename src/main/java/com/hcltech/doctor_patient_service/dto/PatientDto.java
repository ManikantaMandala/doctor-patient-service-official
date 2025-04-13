package com.hcltech.doctor_patient_service.dto;

import com.hcltech.doctor_patient_service.enums.Gender;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class PatientDto {
    private Long id;
    @NotBlank
    private String name;
    private Gender gender;
    @NotBlank
    @Size(min = 10, max = 12)
    private String phone;
    @Min(0)
    private int age;
    private Long doctorId;
    private BasicHealthcareDto basicHealthcareDto;
    private AdvancedHealthcareDto advancedHealthcareDto;

    public PatientDto(long l, String name, Gender gender, String phone, int age) {
        this.id = l;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.age = age;
    }


    public PatientDto() {
        // Default constructor for deserialization
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientDto that = (PatientDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(gender, that.gender);
    }

    public int hashCode() {
        return Objects.hash(id, name, doctorId);
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

    public @NotNull Gender getGender() {
        return gender;
    }

    public void setGender(@NotNull Gender gender) {
        this.gender = gender;
    }

    public @NotBlank @Size(min = 10, max = 12) String getPhone() {
        return phone;
    }

    public void setPhone(@NotBlank @Size(min = 10, max = 12) String phone) {
        this.phone = phone;
    }

    @Min(0)
    public int getAge() {
        return age;
    }

    public void setAge(@Min(0) int age) {
        this.age = age;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public BasicHealthcareDto getBasicHealthcareDto() {
        return basicHealthcareDto;
    }

    public void setBasicHealthcareDto(BasicHealthcareDto basicHealthcareDto) {
        this.basicHealthcareDto = basicHealthcareDto;
    }

    public AdvancedHealthcareDto getAdvancedHealthcareDto() {
        return advancedHealthcareDto;
    }

    public void setAdvancedHealthcareDto(AdvancedHealthcareDto advancedHealthcareDto) {
        this.advancedHealthcareDto = advancedHealthcareDto;
    }


}