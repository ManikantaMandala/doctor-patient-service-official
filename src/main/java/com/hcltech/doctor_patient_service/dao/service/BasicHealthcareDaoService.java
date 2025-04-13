package com.hcltech.doctor_patient_service.dao.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hcltech.doctor_patient_service.entity.BasicHealthcare;
import com.hcltech.doctor_patient_service.repository.BasicHealthcareRepository;

@Service
public class BasicHealthcareDaoService {

    private final BasicHealthcareRepository basicHealthcareRepository;
    private static final Logger log =LoggerFactory.getLogger(BasicHealthcareDaoService.class);

    public BasicHealthcareDaoService(BasicHealthcareRepository basicHealthcareRepository) {
        this.basicHealthcareRepository = basicHealthcareRepository;
    }

    public Optional<BasicHealthcare> getById(Long id) {
    	
    	log.info("fetching BasicHealthcare details With ID: {}",id);
       return  basicHealthcareRepository.findById(id);
    }
}
