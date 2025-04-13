package com.hcltech.doctor_patient_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "chronic_condition")
public class ChronicCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "condition_name")
    private String condition;
    @ManyToOne
    @JoinColumn(name = "advanced_healthcare_id")
    private AdvancedHealthcare advancedHealthcare;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public AdvancedHealthcare getAdvancedHealthcare() {
        return advancedHealthcare;
    }

    public void setAdvancedHealthcare(AdvancedHealthcare advancedHealthcare) {
        this.advancedHealthcare = advancedHealthcare;
    }
}
