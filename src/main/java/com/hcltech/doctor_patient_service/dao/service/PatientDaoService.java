package com.hcltech.doctor_patient_service.dao.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hcltech.doctor_patient_service.entity.Patient;
import com.hcltech.doctor_patient_service.repository.PatientRepository;

@Service
public class PatientDaoService {
    private final PatientRepository patientRepository;
    
  private static final  Logger log = LoggerFactory.getLogger(PatientDaoService.class);

    public PatientDaoService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient create(Patient patient) {
    	log.info("saving patient: {}",patient);
        return patientRepository.save(patient);
    }

    public Optional<Patient> getById(Long id) {
    	log.info("fetching patient by ID: {}",id);
        return patientRepository.findById(id);
    }
    public List<Patient> getAllById(Iterable<Long> ids) {
        if(ids == null){
        	log.warn("received null ids,defaulting to empty list");
            return Collections.emptyList();
        }
        log.info("Fetching patients by IDs,: {}",ids);
        return patientRepository.findAllById(ids);
    }

    public List<Patient> getByDoctorId(Long id) {
    	  log.info("Fetching patients by Doctor ID: {}",id);
        return patientRepository.findByDoctorId(id);
    }

    public List<Patient> get() {
    	log.info("Fetching all patient Details");
        return patientRepository.findAll();
    }

    public void delete(long id) {
    	log.info("Deleting patient with ID: {}",id);
        patientRepository.deleteById(id);
        log.info("Patinet with ID {} deleted successfully ",id);
    }
    
    public Patient update(Patient patient) {
    	log.info("updating Patient: {}",patient);
        return   patientRepository.save(patient);
      }
}
