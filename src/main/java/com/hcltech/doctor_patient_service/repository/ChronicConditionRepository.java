package com.hcltech.doctor_patient_service.repository;

import com.hcltech.doctor_patient_service.entity.ChronicCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChronicConditionRepository extends JpaRepository<ChronicCondition, Long> {
}
