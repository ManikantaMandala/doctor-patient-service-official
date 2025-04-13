package com.hcltech.doctor_patient_service.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "allergy" )
public class Allergy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "types")
    private String types;
    @ManyToOne
    @JoinColumn(name = "advanced_healthcare_id")
    private AdvancedHealthcare advancedHealthcare;

    public Allergy(Long id, String types, AdvancedHealthcare advancedHealthcare) {
        this.id = id;
        this.types = types;
        this.advancedHealthcare = advancedHealthcare;
    }

    public Allergy() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public AdvancedHealthcare getAdvancedHealthcare() {
        return advancedHealthcare;
    }

    public void setAdvancedHealthcare(AdvancedHealthcare advancedHealthcare) {
        this.advancedHealthcare = advancedHealthcare;
    }
}
