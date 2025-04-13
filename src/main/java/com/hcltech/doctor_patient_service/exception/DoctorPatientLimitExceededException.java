package com.hcltech.doctor_patient_service.exception;


public class DoctorPatientLimitExceededException extends  RuntimeException{

    public DoctorPatientLimitExceededException(String message) {

        super(message);
    }
}
