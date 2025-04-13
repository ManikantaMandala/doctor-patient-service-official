package com.hcltech.doctor_patient_service.dao.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hcltech.doctor_patient_service.entity.Allergy;
import com.hcltech.doctor_patient_service.repository.AllergyRepository;

@Service
public class AllergyDaoService {
	 AllergyRepository allergyRepository;
	   private static final Logger log =LoggerFactory.getLogger(AllergyDaoService.class);

	    public AllergyDaoService(AllergyRepository allergyRepository) {
	        this.allergyRepository = allergyRepository;
	    }

	    public Optional<Allergy> findById(Long id) {
	    	log.info("fetching Allergy details With ID: {}",id);
	    	
	      return     allergyRepository.findById(id);
	    }
}
