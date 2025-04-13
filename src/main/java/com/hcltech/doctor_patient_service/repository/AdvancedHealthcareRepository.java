package com.hcltech.doctor_patient_service.repository;

import com.hcltech.doctor_patient_service.entity.AdvancedHealthcare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvancedHealthcareRepository extends JpaRepository<AdvancedHealthcare, Long> {
}
