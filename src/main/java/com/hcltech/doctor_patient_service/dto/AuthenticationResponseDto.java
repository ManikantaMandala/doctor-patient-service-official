package com.hcltech.doctor_patient_service.dto;

public class AuthenticationResponseDto {

    private String jwt;

    public AuthenticationResponseDto(final String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(final String jwt) {
        this.jwt = jwt;
    }
}
