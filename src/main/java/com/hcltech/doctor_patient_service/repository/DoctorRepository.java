package com.hcltech.doctor_patient_service.repository;

import com.hcltech.doctor_patient_service.entity.Doctor;
import com.hcltech.doctor_patient_service.entity.Medication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Doctor save(Doctor doctor);

    List<Doctor> findAll();
    List<Medication> findAllMedicationsById(Long id); // -> 
}
