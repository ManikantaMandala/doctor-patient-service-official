package com.hcltech.doctor_patient_service.dao.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hcltech.doctor_patient_service.entity.Medication;
import com.hcltech.doctor_patient_service.repository.MedicationRepository;

@Service
public class MedicationDaoService {
	private MedicationRepository medicationRepository;
	
	private static final Logger log =LoggerFactory.getLogger(MedicationDaoService.class);

    public MedicationDaoService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public Optional<Medication> findById(Long id)
    {
    	
    	log.info("fetching Medication details With ID: {}",id);
    	
        return medicationRepository.findById(id);
    }
}
