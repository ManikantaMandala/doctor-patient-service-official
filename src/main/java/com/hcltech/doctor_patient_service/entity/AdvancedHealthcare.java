package com.hcltech.doctor_patient_service.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "advanced_healthcare")
public class AdvancedHealthcare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "heart_disease")
    private boolean heartDisease;
    @Column(name = "diabetes")
    private boolean diabetes;
    @Column(name = "hypertension")
    private boolean hypertension;
    @OneToMany(mappedBy = "advancedHealthcare", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChronicCondition> chronicConditions;
    @OneToMany(mappedBy = "advancedHealthcare", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Allergy> allergies;
    @OneToMany(mappedBy = "advancedHealthcare", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Medication> medications;
    @OneToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isHeartDisease() {
        return heartDisease;
    }

    public void setHeartDisease(boolean heartDisease) {
        this.heartDisease = heartDisease;
    }

    public boolean isDiabetes() {
        return diabetes;
    }

    public void setDiabetes(boolean diabetes) {
        this.diabetes = diabetes;
    }

    public boolean isHypertension() {
        return hypertension;
    }

    public void setHypertension(boolean hypertension) {
        this.hypertension = hypertension;
    }

    public List<ChronicCondition> getChronicConditions() {
        return chronicConditions;
    }

    public void setChronicConditions(List<ChronicCondition> chronicConditions) {
        this.chronicConditions = chronicConditions;
    }

    public List<Allergy> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<Allergy> allergies) {
        this.allergies = allergies;
    }

    public List<Medication> getMedications() {
        return medications;
    }

    public void setMedications(List<Medication> medications) {
        this.medications = medications;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
