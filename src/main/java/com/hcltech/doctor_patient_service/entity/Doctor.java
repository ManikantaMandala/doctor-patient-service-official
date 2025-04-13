package com.hcltech.doctor_patient_service.entity;

import com.hcltech.doctor_patient_service.enums.SpecialistType;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "qualification")
    private String qualification;
    @Column(name = "specialist")
    @Enumerated(EnumType.STRING)
    private SpecialistType specialist;
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Patient> patients;

    public Doctor() {
    }
    public Doctor(Long id, String name, String qualification, SpecialistType specialist, List<Patient> patients) {
        this.id = id;
        this.name = name;
        this.qualification = qualification;
        this.specialist = specialist;
        this.patients = patients;
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

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public SpecialistType getSpecialist() {
        return specialist;
    }

    public void setSpecialist(SpecialistType specialist) {
        this.specialist = specialist;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }
}
