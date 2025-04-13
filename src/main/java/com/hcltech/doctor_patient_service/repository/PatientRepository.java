package com.hcltech.doctor_patient_service.repository;

import com.hcltech.doctor_patient_service.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("SELECT p FROM Patient p WHERE p.doctor.id = :id")
    List<Patient> findByDoctorId(@Param("id") Long id);
}
