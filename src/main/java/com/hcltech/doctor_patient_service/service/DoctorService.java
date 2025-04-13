package com.hcltech.doctor_patient_service.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.hcltech.doctor_patient_service.exception.PatientNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcltech.doctor_patient_service.dao.service.DoctorDaoService;
import com.hcltech.doctor_patient_service.dao.service.PatientDaoService;
import com.hcltech.doctor_patient_service.dto.DoctorDto;
import com.hcltech.doctor_patient_service.entity.Doctor;
import com.hcltech.doctor_patient_service.entity.Patient;
import com.hcltech.doctor_patient_service.exception.DoctorNotFoundException;
import com.hcltech.doctor_patient_service.exception.DoctorPatientLimitExceededException;

@Service
public class DoctorService {
    private final DoctorDaoService doctorDaoService;
    private final PatientDaoService patientDaoService;

    private static final Logger log = LoggerFactory.getLogger(DoctorService.class);
    private static final String MAX_PATIENT_LIMIT_EXCEEDED = "Doctor has exceeded the maximum patient limit of 4";

    @Autowired
    public DoctorService(DoctorDaoService doctorDaoService, PatientDaoService patientDaoService) {
        this.doctorDaoService = doctorDaoService;
        this.patientDaoService = patientDaoService;
    }


    public DoctorDto create(DoctorDto doctorDto) {
        if (doctorDto.getPatientIds() != null) {
            for (Long id : doctorDto.getPatientIds()) {
                Optional<Patient> patient = patientDaoService.getById(id);
                if (patient.isEmpty()) {
                    throw new PatientNotFoundException("Patient not found");
                }
            }
            if (doctorDto.getPatientIds().size() > 4) {
                log.warn(MAX_PATIENT_LIMIT_EXCEEDED);
                throw new DoctorPatientLimitExceededException(MAX_PATIENT_LIMIT_EXCEEDED);
            }
        }
        Doctor doctor = toEntity(doctorDto);
        log.info("doctor: {} created suceessfully", doctor);
        return toDto(doctorDaoService.create(doctor));
    }

    public DoctorDto getById(Long id) {

        return doctorDaoService.getById(id).map(this::toDto).orElseThrow(() -> new DoctorNotFoundException("Doctor not found for Id:" + id));
    }

    public List<DoctorDto> get() {
        List<DoctorDto> doctorDtos = doctorDaoService.get().stream().map(this::toDto).collect(Collectors.toList());
        log.info("successfully fetching all doctor details: {}", doctorDtos.size());
        return doctorDtos;
    }

    private Doctor toEntity(DoctorDto doctorDto) {
        Doctor doctor = new Doctor();
        doctor.setId(doctorDto.getId());
        doctor.setName(doctorDto.getName());
        doctor.setQualification(doctorDto.getQualification());
        doctor.setSpecialist(doctorDto.getSpecialist());
        List<Patient> patients = patientDaoService.getAllById(doctorDto.getPatientIds());
        for (Patient patient : patients) {
            patient.setDoctor(doctor);
        }
        doctor.setPatients(patients);
        return doctor;
    }

    private DoctorDto toDto(Doctor doctor) {
        DoctorDto doctorDto = new DoctorDto();
        doctorDto.setId(doctor.getId());
        doctorDto.setName(doctor.getName());
        doctorDto.setQualification(doctor.getQualification());
        doctorDto.setSpecialist(doctor.getSpecialist());
        List<Long> patientIds = doctor.getPatients().stream().map(Patient::getId).toList();
        doctorDto.setPatientIds(patientIds);
        return doctorDto;
    }

    public int noOfPatients(Long doctorId) {
        Optional<Doctor> byId = doctorDaoService.getById(doctorId);
        int size = 0;
        if (byId.isPresent()) {
            size = byId.get().getPatients().size();
        }
        return size;
    }

    public boolean assignPatient(Long doctorId, Long patientId) {
        return doctorDaoService.assignPatient(doctorId, patientId);
    }

    public void unassignPatient(Long doctorId, Long patientId) {
        doctorDaoService.unassignPatient(doctorId, patientId);
    }

    public Boolean delete(long id) {

        doctorDaoService.delete(id);
        return true;
    }

    public Optional<DoctorDto> update(DoctorDto doctorDto) {
        if (doctorDto.getPatientIds() != null && doctorDto.getPatientIds().size() > 4) {
            throw new DoctorPatientLimitExceededException(MAX_PATIENT_LIMIT_EXCEEDED);
        }

        Optional<Object> optionalDoctor = doctorDaoService.createDoctor(toEntity(doctorDto));
        if (optionalDoctor.isPresent()) {
            return Optional.of(toDto((Doctor) optionalDoctor.get()));
        }
        return Optional.empty();
    }
}
