package com.hcltech.doctor_patient_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
public class InvalidLoginCredentialException extends RuntimeException {

    public InvalidLoginCredentialException(String message) {
        super(message);
    }
}
