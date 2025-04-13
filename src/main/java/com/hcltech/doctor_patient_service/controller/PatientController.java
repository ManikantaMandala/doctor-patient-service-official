package com.hcltech.doctor_patient_service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hcltech.doctor_patient_service.dto.PatientDto;
import com.hcltech.doctor_patient_service.service.PatientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/doctor-patient-service/v1/patients")
public class PatientController {
    private final PatientService patientService;
    
    private static final Logger log = LoggerFactory.getLogger(PatientController.class);

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<PatientDto> create(@RequestBody @Valid PatientDto patientDto) {
    	log.info("Receive request to save patient details:{}",patientDto);
        PatientDto createdPatient = patientService.create(patientDto);
        log.info("Patient: {}  details saves successfully",createdPatient);
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getById(@PathVariable Long id) {
    	log.info("Receive request to get patient details with ID: {}",id);
        PatientDto patientDTO = patientService.getById(id);
        log.info("Patient: {}  detail retrieved successfully",patientDTO);
        if (patientDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(patientDTO, HttpStatus.OK);
        }
    }

    @GetMapping
    public ResponseEntity<List<PatientDto>> get() {
    	log.info("Receive request to get all Patient deatils");
        List<PatientDto> patientDTO = patientService.get();
        log.info("successfully retrieved Patients details: {}",patientDTO.size());
        return new ResponseEntity<>(patientDTO, HttpStatus.OK);
    }
    
    @PutMapping("/id/{id}")
    public ResponseEntity<PatientDto> update(@PathVariable Long id,@RequestBody PatientDto patientDto ) {
    	log.info("Received request to update Patient details with ID:{}",id);
        PatientDto updatedDto =  patientService.update(id,patientDto);
        log.info("update successfuly with ID: {}",id);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        boolean deleted = patientService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
