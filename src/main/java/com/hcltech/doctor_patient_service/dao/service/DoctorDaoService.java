package com.hcltech.doctor_patient_service.dao.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hcltech.doctor_patient_service.entity.Doctor;
import com.hcltech.doctor_patient_service.entity.Patient;
import com.hcltech.doctor_patient_service.exception.DoctorNotFoundException;
import com.hcltech.doctor_patient_service.exception.DoctorPatientLimitExceededException;
import com.hcltech.doctor_patient_service.exception.PatientNotAssignedException;
import com.hcltech.doctor_patient_service.exception.PatientNotFoundException;
import com.hcltech.doctor_patient_service.repository.DoctorRepository;
import com.hcltech.doctor_patient_service.repository.PatientRepository;

@Service
public class DoctorDaoService {
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    
    private static final Logger log = LoggerFactory.getLogger(DoctorDaoService.class);

    public DoctorDaoService(DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public Doctor create(Doctor doctor) {
    	Doctor savedDoctor =  doctorRepository.save(doctor);
    	log.info("Doctor saved successfully");
    	return savedDoctor;
    }

    public Optional<Doctor> getById(Long id) {
        if(id == null)
            id=0l;
        return doctorRepository.findById(id);
    }

    public List<Doctor> get() {
    	 List<Doctor> doctors = doctorRepository.findAll();
    	 log.info("fetching successfully");
    	 return doctors;
    }

    public boolean assignPatient(Long doctorId, Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() ->  new PatientNotFoundException("Patient not found"));
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new DoctorNotFoundException("Doctor not found"));

        if(doctor.getPatients()==null){
            doctor.setPatients(new ArrayList<>());
        }

        if(doctor.getPatients().size() >= 4){
        	log.warn("Doctor: {} has exceeded the maximum patient: {} limit of 4",doctorId,patientId);
            throw new DoctorPatientLimitExceededException("Doctor has exceeded the maximum patient limit of 4");
        }

        patient.setDoctor(doctor);

        patientRepository.save(patient);
        log.info("Suceessfully assigned patient {} to doctor {}",patientId,doctorId);

        return true;
    }

    public void unassignPatient(Long doctorId, Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new DoctorNotFoundException("Doctor not found"));

        if(doctor.getPatients()==null){
            doctor.setPatients(new ArrayList<>());
        }

        if(!doctor.getPatients().contains(patient)){
        	log.warn("patient {} is not assigned to this doctor{}",patientId,doctorId);
            throw new PatientNotAssignedException("patient is not assigned to this doctor");
        }

        patient.setDoctor(null);

        patientRepository.save(patient);
        log.info("Suceessfully unassigned patient {} to doctor {}",patientId,doctorId);
    }

    public void delete(long id) {
    	log.info("deleting doctor with ID: {}",id);
    	
        doctorRepository.deleteById( id);
        log.info("doctor with ID: {} deleted successfull",id);
    }

    public Optional<Object> createDoctor(Doctor doctor) {
        if (doctor == null) {
            throw new IllegalArgumentException("doctor can't be null");
        }

        try {
            Object savedDoctor =  doctorRepository.save(doctor);
            return Optional.of(savedDoctor);
        } catch (Exception e) {

            log.error("Error saving doctor: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

}
