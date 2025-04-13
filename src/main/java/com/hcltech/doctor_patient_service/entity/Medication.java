package com.hcltech.doctor_patient_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "medication")
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "dosage")
    private String dosage;
    @ManyToOne
    @JoinColumn(name = "advanced_healthcare_id")
    private AdvancedHealthcare advancedHealthcare;

    public Medication(Long id, String name, String dosage, AdvancedHealthcare advancedHealthcare) {
        this.id = id;
        this.name = name;
        this.dosage = dosage;
        this.advancedHealthcare = advancedHealthcare;
    }

    public Medication() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public AdvancedHealthcare getAdvancedHealthcare() {
        return advancedHealthcare;
    }

    public void setAdvancedHealthcare(AdvancedHealthcare advancedHealthcare) {
        this.advancedHealthcare = advancedHealthcare;
    }
}