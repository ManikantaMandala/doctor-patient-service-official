package com.hcltech.doctor_patient_service.service;

import com.hcltech.doctor_patient_service.dao.service.*;
import com.hcltech.doctor_patient_service.dto.*;
import com.hcltech.doctor_patient_service.entity.*;
import com.hcltech.doctor_patient_service.exception.DoctorPatientLimitExceededException;
import com.hcltech.doctor_patient_service.exception.PatientNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientDaoService patientDaoService;

    @Mock
    private DoctorDaoService doctorDaoService;

    @Mock
    private DoctorService doctorService;

    @Mock
    private BasicHealthcareDaoService basicHealthcareDaoService;

    @Mock
    private AdvancedHealthcareDaoService advancedHealthcareDaoService;

    @Mock
    private ChronicConditionDaoService chronicConditionDaoService;

    @Mock
    private  AllergyDaoService allergyDaoService;

    @Mock
    private MedicationDaoService medicationDaoService;

    @InjectMocks
    private PatientService patientService;

    private Patient patient;
    private PatientDto patientDto;
    private Doctor doctor;

    @BeforeEach
    void setUp() {
        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Dr. Smith");

        patient = new Patient();
        patient.setId(1L);
        patient.setName("John Doe");
        patient.setDoctor(doctor);
        patient.setBasicHealthcare(new BasicHealthcare());

        AdvancedHealthcare advancedHealthcare = new AdvancedHealthcare();
        advancedHealthcare.setChronicConditions(new ArrayList<>()); // Initialize the chronicConditions list
        advancedHealthcare.setAllergies(new ArrayList<>()); // Initialize the allergies list
        advancedHealthcare.setMedications(new ArrayList<>()); // Initialize the medications list
        patient.setAdvancedHealthcare(advancedHealthcare);

        patientDto = new PatientDto();
        patientDto.setId(1L);
        patientDto.setName("John Doe");
        patientDto.setDoctorId(1L);
        patientDto.setBasicHealthcareDto(new BasicHealthcareDto());

        AdvancedHealthcareDto advancedHealthcareDto = new AdvancedHealthcareDto();
        advancedHealthcareDto.setChronicConditionDto(new ArrayList<>()); // Initialize the chronicConditionDto list
        advancedHealthcareDto.setAllergyDto(new ArrayList<>()); // Initialize the allergyDto list
        advancedHealthcareDto.setMedicationDto(new ArrayList<>()); // Initialize the medicationDto list
        patientDto.setAdvancedHealthcareDto(advancedHealthcareDto);
    }

    @Test
    @DisplayName("Test_GetPatientById")
    void testGetPatientById() {
        when(patientDaoService.getById(1L)).thenReturn(Optional.of(patient));

        PatientDto foundPatientDto = patientService.getById(1L);

        assertNotNull(foundPatientDto);
        assertEquals("John Doe", foundPatientDto.getName());
        verify(patientDaoService, times(1)).getById(1L);
    }

    @Test
    @DisplayName("Test_GetAllPatients")
    void testGetAllPatients() {
        List<Patient> patients = Collections.singletonList(patient);
        when(patientDaoService.get()).thenReturn(patients);

        List<PatientDto> foundPatients = patientService.get();

        assertEquals(1, foundPatients.size());
        assertEquals("John Doe", foundPatients.get(0).getName());
        verify(patientDaoService, times(1)).get();
    }

    @Test
    @DisplayName("Test_UpdatePatient")
    void testUpdatePatient() {
        when(patientDaoService.getById(1L)).thenReturn(Optional.of(patient));
        when(patientDaoService.update(any(Patient.class))).thenReturn(patient);

        PatientDto updatedPatientDto = patientService.update(1L, patientDto);

        assertNotNull(updatedPatientDto);
        assertEquals("John Doe", updatedPatientDto.getName());
        verify(patientDaoService, times(1)).update(any(Patient.class));
    }

    @Test
    @DisplayName("Test_UpdatePatient_NotFound")
    void testUpdatePatientNotFound() {
        when(patientDaoService.getById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(PatientNotFoundException.class, () ->
                patientService.update(1L, patientDto));

        assertEquals("Patient not found for ID: 1", exception.getMessage());
    }

    @Test
    @DisplayName("Test_DeletePatient")
    void testDeletePatient() {
        doNothing().when(patientDaoService).delete(1L);

        boolean result = patientService.delete(1L);

        assertTrue(result);
        verify(patientDaoService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("Test_GetPatientsByDoctorId")
    void testGetPatientsByDoctorId() {
        List<Patient> patients = Collections.singletonList(patient);
        when(patientDaoService.getByDoctorId(1L)).thenReturn(patients);

        List<PatientDto> foundPatients = patientService.getByDoctorId(1L);

        assertEquals(1, foundPatients.size());
        assertEquals("John Doe", foundPatients.get(0).getName());
        verify(patientDaoService, times(1)).getByDoctorId(1L);
    }

    @Test
    @DisplayName("Test_UpdateChronicConditions")
    void testUpdateChronicConditions() {
        AdvancedHealthcare advancedHealthcare = new AdvancedHealthcare();
        advancedHealthcare.setChronicConditions(new ArrayList<>());

        ChronicConditionDto chronicConditionDto = new ChronicConditionDto();
        chronicConditionDto.setId(1L);
        chronicConditionDto.setCondition("Diabetes");

        List<ChronicConditionDto> chronicConditionDtos = Collections.singletonList(chronicConditionDto);

        patientService.updateChronicConditions(advancedHealthcare, chronicConditionDtos);

        assertEquals(1, advancedHealthcare.getChronicConditions().size());
        assertEquals("Diabetes", advancedHealthcare.getChronicConditions().get(0).getCondition());
    }

    @Test
    @DisplayName("Test_UpdateAllergies")
    void testUpdateAllergies() {
        AdvancedHealthcare advancedHealthcare = new AdvancedHealthcare();
        advancedHealthcare.setAllergies(new ArrayList<>());

        AllergyDto allergyDto = new AllergyDto();
        allergyDto.setId(1L);
        allergyDto.setTypes("Pollen");

        List<AllergyDto> allergyDtos = Collections.singletonList(allergyDto);

        patientService.updateAllergies(advancedHealthcare, allergyDtos);

        assertEquals(1, advancedHealthcare.getAllergies().size());
        assertEquals("Pollen", advancedHealthcare.getAllergies().get(0).getTypes());
    }

    @Test
    @DisplayName("Test_UpdateMedications")
    void testUpdateMedications() {
        AdvancedHealthcare advancedHealthcare = new AdvancedHealthcare();
        advancedHealthcare.setMedications(new ArrayList<>());

        MedicationDto medicationDto = new MedicationDto();
        medicationDto.setId(1L);
        medicationDto.setName("Aspirin");
        medicationDto.setDosage("100mg");

        List<MedicationDto> medicationDtos = Collections.singletonList(medicationDto);

        patientService.updateMedications(advancedHealthcare, medicationDtos);

        assertEquals(1, advancedHealthcare.getMedications().size());
        assertEquals("Aspirin", advancedHealthcare.getMedications().get(0).getName());
        assertEquals("100mg", advancedHealthcare.getMedications().get(0).getDosage());
    }
}