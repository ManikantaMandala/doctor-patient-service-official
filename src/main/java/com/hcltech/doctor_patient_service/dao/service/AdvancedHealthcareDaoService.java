package com.hcltech.doctor_patient_service.dao.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hcltech.doctor_patient_service.entity.AdvancedHealthcare;
import com.hcltech.doctor_patient_service.repository.AdvancedHealthcareRepository;

@Service
public class AdvancedHealthcareDaoService {
	 AdvancedHealthcareRepository advancedHealthcareRepository;
	 
	  private static final Logger log =LoggerFactory.getLogger(AdvancedHealthcareDaoService.class);

	    public AdvancedHealthcareDaoService(AdvancedHealthcareRepository advancedHealthcareRepository) {
	        this.advancedHealthcareRepository = advancedHealthcareRepository;
	    }

	    public Optional<AdvancedHealthcare> getById(Long id) {
	    	log.info("fetching AdvancedHealthcare details With ID: {}",id);
	       return advancedHealthcareRepository.findById(id);
	    }
}
