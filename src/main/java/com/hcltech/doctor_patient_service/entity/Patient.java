package com.hcltech.doctor_patient_service.entity;

import com.hcltech.doctor_patient_service.enums.Gender;
import jakarta.persistence.*;

@Entity
@Table(name = "patient")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name = "phone")
    private String phone;
    @Column(name = "age")
    private int age;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private BasicHealthcare basicHealthcare;
    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private AdvancedHealthcare advancedHealthcare;

    public Patient(Long id, String name, Gender gender, String phone, int age) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.age = age;

    }



    public Patient() {
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public BasicHealthcare getBasicHealthcare() {
        return basicHealthcare;
    }

    public void setBasicHealthcare(BasicHealthcare basicHealthcare) {
        this.basicHealthcare = basicHealthcare;
    }

    public AdvancedHealthcare getAdvancedHealthcare() {
        return advancedHealthcare;
    }

    public void setAdvancedHealthcare(AdvancedHealthcare advancedHealthcare) {
        this.advancedHealthcare = advancedHealthcare;
    }
}
