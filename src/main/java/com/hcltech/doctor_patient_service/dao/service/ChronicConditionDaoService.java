package com.hcltech.doctor_patient_service.dao.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hcltech.doctor_patient_service.entity.ChronicCondition;
import com.hcltech.doctor_patient_service.repository.ChronicConditionRepository;

@Service
public class ChronicConditionDaoService {
	private final ChronicConditionRepository chronicConditionRepository;
	
	private static final Logger log =LoggerFactory.getLogger(ChronicConditionDaoService.class);
	

    public ChronicConditionDaoService(ChronicConditionRepository chronicConditionRepository) {
        this.chronicConditionRepository = chronicConditionRepository;
    }

    public Optional<ChronicCondition> findById(Long id) {
    	log.info("fetching ChronicCondition details With ID: {}",id);
      return   chronicConditionRepository.findById(id);
    }
}
