package com.hcltech.doctor_patient_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.hcltech.doctor_patient_service.exception.DoctorNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.hcltech.doctor_patient_service.dao.service.AdvancedHealthcareDaoService;
import com.hcltech.doctor_patient_service.dao.service.BasicHealthcareDaoService;
import com.hcltech.doctor_patient_service.dao.service.DoctorDaoService;
import com.hcltech.doctor_patient_service.dao.service.PatientDaoService;
import com.hcltech.doctor_patient_service.dto.AdvancedHealthcareDto;
import com.hcltech.doctor_patient_service.dto.AllergyDto;
import com.hcltech.doctor_patient_service.dto.BasicHealthcareDto;
import com.hcltech.doctor_patient_service.dto.ChronicConditionDto;
import com.hcltech.doctor_patient_service.dto.MedicationDto;
import com.hcltech.doctor_patient_service.dto.PatientDto;
import com.hcltech.doctor_patient_service.entity.AdvancedHealthcare;
import com.hcltech.doctor_patient_service.entity.Allergy;
import com.hcltech.doctor_patient_service.entity.BasicHealthcare;
import com.hcltech.doctor_patient_service.entity.ChronicCondition;
import com.hcltech.doctor_patient_service.entity.Doctor;
import com.hcltech.doctor_patient_service.entity.Medication;
import com.hcltech.doctor_patient_service.entity.Patient;
import com.hcltech.doctor_patient_service.exception.DoctorPatientLimitExceededException;
import com.hcltech.doctor_patient_service.exception.PatientNotFoundException;
import jakarta.validation.Valid;

@Service
public class PatientService {
    private final PatientDaoService patientDaoService;
    private final DoctorDaoService doctorDaoService;
    private final BasicHealthcareDaoService basicHealthcareDaoService;
    private final AdvancedHealthcareDaoService advancedHealthcareDaoService;


    private static final Logger log = LoggerFactory.getLogger(PatientService.class);

    public PatientService(PatientDaoService patientDaoService, DoctorDaoService doctorDaoService, BasicHealthcareDaoService basicHealthcareDaoService, AdvancedHealthcareDaoService advancedHealthcareDaoService) {
        this.patientDaoService = patientDaoService;
        this.doctorDaoService = doctorDaoService;
        this.basicHealthcareDaoService = basicHealthcareDaoService;
        this.advancedHealthcareDaoService = advancedHealthcareDaoService;


    }

    public PatientDto create(@Valid PatientDto patientDto) {
        Patient patient = toEntity(patientDto);

        if (patientDto.getDoctorId()!= null) {
            Optional<Doctor> doctor = doctorDaoService.getById(patientDto.getDoctorId());
            if (doctor.isEmpty()) {
                throw new DoctorNotFoundException("Doctor not found");
            }
            if (doctor.get().getPatients().size() > 3) {
                log.warn("Doctor already have 4 patients");
                throw new DoctorPatientLimitExceededException("Doctor already have 4 patients");
            }
        }
        Patient savedPatient = patientDaoService.create(patient);
        log.info("Doctor: {} details saved successfully", savedPatient);

        return toDTO(savedPatient);
    }

    public PatientDto getById(Long id) {
        Optional<Patient> patient = patientDaoService.getById(id);
        return patient.map(this::toDTO).orElse(null);
    }

    private Patient toEntity(PatientDto patientDto) {
        Patient patient = new Patient();
        patient.setName(patientDto.getName());
        patient.setAge(patientDto.getAge());
        patient.setPhone(patientDto.getPhone());
        patient.setGender(patientDto.getGender());

        Optional<Doctor> doctor = doctorDaoService.getById(patientDto.getDoctorId());
        doctor.ifPresent(patient::setDoctor);
        if (patientDto.getBasicHealthcareDto() != null) {
            BasicHealthcare basicHealthcare = getBasicHealthcare(patientDto);
            basicHealthcare.setPatient(patient);
            patient.setBasicHealthcare(basicHealthcare);
        }

        if (patientDto.getAdvancedHealthcareDto() != null) {
            AdvancedHealthcare advancedHealthcare = new AdvancedHealthcare();
            advancedHealthcare.setHeartDisease(patientDto.getAdvancedHealthcareDto().isHeartDisease());
            advancedHealthcare.setDiabetes(patientDto.getAdvancedHealthcareDto().isDiabetes());
            advancedHealthcare.setHypertension(patientDto.getAdvancedHealthcareDto().isHypertension());
            advancedHealthcare.setChronicConditions(patientDto.getAdvancedHealthcareDto().getChronicConditionDto().stream()
                    .map(chronicDTO -> {
                        ChronicCondition chronic = new ChronicCondition();
                        chronic.setCondition(chronicDTO.getCondition());
                        chronic.setAdvancedHealthcare(advancedHealthcare);
                        return chronic;
                    }).toList());
            advancedHealthcare.setAllergies(patientDto.getAdvancedHealthcareDto().getAllergyDto().stream()
                    .map(allergyDTO -> {
                        Allergy allergy = new Allergy();
                        allergy.setTypes(allergyDTO.getTypes());
                        allergy.setAdvancedHealthcare(advancedHealthcare);
                        return allergy;
                    }).toList());
            advancedHealthcare.setMedications(patientDto.getAdvancedHealthcareDto().getMedicationDto().stream()
                    .map(medicationDTO -> {
                        Medication medication = new Medication();
                        medication.setName(medicationDTO.getName());
                        medication.setDosage(medicationDTO.getDosage());
                        medication.setAdvancedHealthcare(advancedHealthcare);
                        return medication;
                    }).toList());
            advancedHealthcare.setPatient(patient);
            patient.setAdvancedHealthcare(advancedHealthcare);
        }
        return patient;
    }

    private static BasicHealthcare getBasicHealthcare(PatientDto patientDto) {
        BasicHealthcare basicHealthcare = new BasicHealthcare();
        basicHealthcare.setHeight(patientDto.getBasicHealthcareDto().getHeight());
        basicHealthcare.setWeight(patientDto.getBasicHealthcareDto().getWeight());
        basicHealthcare.setBloodPressure(patientDto.getBasicHealthcareDto().getBloodPressure());
        basicHealthcare.setSugar(patientDto.getBasicHealthcareDto().getSugar());
        basicHealthcare.setBloodGroup(patientDto.getBasicHealthcareDto().getBloodGroup());
        return basicHealthcare;
    }

    private PatientDto toDTO(Patient patient) {
        PatientDto patientDto = new PatientDto();
        patientDto.setId(patient.getId());
        patientDto.setName(patient.getName());
        patientDto.setAge(patient.getAge());
        patientDto.setPhone(patient.getPhone());
        patientDto.setGender(patient.getGender());
        patientDto.setDoctorId(patient.getDoctor() != null ? patient.getDoctor().getId() : null);
        if (patient.getBasicHealthcare() != null) {
            BasicHealthcareDto basicHealthcareDto = getBasicHealthcareDto(patient);
            patientDto.setBasicHealthcareDto(basicHealthcareDto);
        }
        if (patient.getAdvancedHealthcare() != null) {
            AdvancedHealthcareDto advancedDto = new AdvancedHealthcareDto();
            advancedDto.setId(patient.getAdvancedHealthcare().getId());
            advancedDto.setHeartDisease(patient.getAdvancedHealthcare().isHeartDisease());
            advancedDto.setDiabetes(patient.getAdvancedHealthcare().isDiabetes());
            advancedDto.setHypertension(patient.getAdvancedHealthcare().isHypertension());
            advancedDto.setChronicConditionDto(patient.getAdvancedHealthcare().getChronicConditions().stream()
                    .map(condition -> {
                        ChronicConditionDto chronicDTO = new ChronicConditionDto();
                        chronicDTO.setId(condition.getId());
                        chronicDTO.setCondition(condition.getCondition());
                        return chronicDTO;
                    }).toList());
            advancedDto.setAllergyDto(patient.getAdvancedHealthcare().getAllergies().stream()
                    .map(allergy -> {
                        AllergyDto allergyDTO = new AllergyDto();
                        allergyDTO.setId(allergy.getId());
                        allergyDTO.setTypes(allergy.getTypes());
                        return allergyDTO;
                    }).toList());
            advancedDto.setMedicationDto(patient.getAdvancedHealthcare().getMedications().stream()
                    .map(medication -> {
                        MedicationDto medicationDto = new MedicationDto();
                        medicationDto.setId(medication.getId());
                        medicationDto.setName(medication.getName());
                        medicationDto.setDosage(medication.getDosage());
                        return medicationDto;
                    }).toList());
            patientDto.setAdvancedHealthcareDto(advancedDto);
        }
        return patientDto;
    }

    private static BasicHealthcareDto getBasicHealthcareDto(Patient patient) {
        BasicHealthcareDto basicHealthcareDto = new BasicHealthcareDto();
        basicHealthcareDto.setId(patient.getBasicHealthcare().getId());
        basicHealthcareDto.setHeight(patient.getBasicHealthcare().getHeight());
        basicHealthcareDto.setWeight(patient.getBasicHealthcare().getWeight());
        basicHealthcareDto.setSugar(patient.getBasicHealthcare().getSugar());
        basicHealthcareDto.setBloodPressure(patient.getBasicHealthcare().getBloodPressure());
        basicHealthcareDto.setBloodGroup(patient.getBasicHealthcare().getBloodGroup());
        return basicHealthcareDto;
    }

    public List<PatientDto> getByDoctorId(Long id) {
        return toDTO(patientDaoService.getByDoctorId(id));
    }

    private List<PatientDto> toDTO(List<Patient> patients) {
        return patients.stream()
                .map(this::toDTO)
                .toList();
    }

    public List<PatientDto> get() {
        log.info("getting patient deatils ");
        return patientDaoService.get().stream().map(this::toDTO).toList();
    }

    public Boolean delete(long id) {

        patientDaoService.delete(id);
        log.info("delete patient details successfulyy with ID: {}", id);
        return true;
    }


    public PatientDto update(Long id, PatientDto patientDto) {
        Optional<Patient> optional = patientDaoService.getById(id);

        if (optional.isPresent()) {
            Patient patient = optional.get();

            patient.setName(patientDto.getName());
            patient.setAge(patientDto.getAge());
            patient.setPhone(patientDto.getPhone());
            patient.setGender(patientDto.getGender());

            Optional<BasicHealthcare> optionalBasicHealthcare = basicHealthcareDaoService.getById(patient.getBasicHealthcare().getId());
            if (optionalBasicHealthcare.isPresent()) {
                BasicHealthcare basicHealthcare = optionalBasicHealthcare.get();
                basicHealthcare.setHeight(patientDto.getBasicHealthcareDto().getHeight());
                basicHealthcare.setWeight(patientDto.getBasicHealthcareDto().getWeight());
                basicHealthcare.setBloodPressure(patientDto.getBasicHealthcareDto().getBloodPressure());
                basicHealthcare.setSugar(patientDto.getBasicHealthcareDto().getSugar());
                basicHealthcare.setBloodGroup(patientDto.getBasicHealthcareDto().getBloodGroup());
                basicHealthcare.setPatient(patient);
                patient.setBasicHealthcare(basicHealthcare);
            }

            Optional<AdvancedHealthcare> optionalAdvancedHealthcare = advancedHealthcareDaoService.getById(patient.getAdvancedHealthcare().getId());
            if (optionalAdvancedHealthcare.isPresent()) {
                AdvancedHealthcare advancedHealthcare = optionalAdvancedHealthcare.get();
                advancedHealthcare.setHeartDisease(patientDto.getAdvancedHealthcareDto().isHeartDisease());
                advancedHealthcare.setDiabetes(patientDto.getAdvancedHealthcareDto().isDiabetes());
                advancedHealthcare.setHypertension(patientDto.getAdvancedHealthcareDto().isHypertension());

                updateChronicConditions(advancedHealthcare, patientDto.getAdvancedHealthcareDto().getChronicConditionDto());

                updateAllergies(advancedHealthcare, patientDto.getAdvancedHealthcareDto().getAllergyDto());

                updateMedications(advancedHealthcare, patientDto.getAdvancedHealthcareDto().getMedicationDto());

                advancedHealthcare.setPatient(patient);
                patient.setAdvancedHealthcare(advancedHealthcare);
            }

            Patient updatedPatient = patientDaoService.update(patient);
            log.info("patient details are updated successfully ");
            return toDTO(updatedPatient);
        } else {
            log.warn("Patient not found for ID: {}", id);
            throw new PatientNotFoundException("Patient not found for ID: " + id);
        }
    }


    public void updateChronicConditions(AdvancedHealthcare advancedHealthcare, List<ChronicConditionDto> chronicConditionDtos) {
        List<ChronicCondition> existingChronicConditions = advancedHealthcare.getChronicConditions();
        List<ChronicCondition> updatedChronicConditions = new ArrayList<>();

        for (ChronicConditionDto chronicDTO : chronicConditionDtos) {
            ChronicCondition chronicCondition = existingChronicConditions.stream()
                    .filter(c -> c.getId().equals(chronicDTO.getId()))
                    .findFirst()
                    .orElse(new ChronicCondition()); // Create if not found

            chronicCondition.setCondition(chronicDTO.getCondition());
            chronicCondition.setAdvancedHealthcare(advancedHealthcare);
            updatedChronicConditions.add(chronicCondition);
        }

        existingChronicConditions.clear();
        existingChronicConditions.addAll(updatedChronicConditions);
    }

    public void updateAllergies(AdvancedHealthcare advancedHealthcare, List<AllergyDto> allergyDtos) {
        List<Allergy> existingAllergies = advancedHealthcare.getAllergies();
        List<Allergy> updatedAllergies = new ArrayList<>();

        for (AllergyDto allergyDTO : allergyDtos) {
            Allergy allergy = existingAllergies.stream()
                    .filter(a -> a.getId().equals(allergyDTO.getId()))
                    .findFirst()
                    .orElse(new Allergy()); // Create if not found

            allergy.setTypes(allergyDTO.getTypes());
            allergy.setAdvancedHealthcare(advancedHealthcare);
            updatedAllergies.add(allergy);
        }

        existingAllergies.clear();
        existingAllergies.addAll(updatedAllergies);
    }

    public void updateMedications(AdvancedHealthcare advancedHealthcare, List<MedicationDto> medicationDtos) {
        List<Medication> existingMedications = advancedHealthcare.getMedications();
        List<Medication> updatedMedications = new ArrayList<>();

        for (MedicationDto medicationDTO : medicationDtos) {
            Medication medication = existingMedications.stream()
                    .filter(m -> m.getId().equals(medicationDTO.getId()))
                    .findFirst()
                    .orElse(new Medication()); // Create if not found

            medication.setName(medicationDTO.getName());
            medication.setDosage(medicationDTO.getDosage());
            medication.setAdvancedHealthcare(advancedHealthcare);
            updatedMedications.add(medication);
        }

        existingMedications.clear();
        existingMedications.addAll(updatedMedications);
    }

}
