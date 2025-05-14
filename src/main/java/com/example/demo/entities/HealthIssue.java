package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

@Entity
@Table(name = "health_issues")
public class HealthIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Health issue name is required")
    private String name;

    private String description;

    @NotNull(message = "Diagnosis date is required")
    @PastOrPresent(message = "Diagnosis date cannot be in the future")
    @Column(name = "diagnosis_date")
    private LocalDate diagnosisDate;

    @Column(name = "recovery_date")
    private LocalDate recoveryDate;

    @Column(name = "treatment")
    private String treatment;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;

    protected HealthIssue() {
    }

    public HealthIssue(@NotBlank(message = "Health issue name is required") String name, String description,
            @NotNull(message = "Diagnosis date is required") @PastOrPresent(message = "Diagnosis date cannot be in the future") LocalDate diagnosisDate,
            LocalDate recoveryDate, String treatment, Animal animal) {
        this.name = name;
        this.description = description;
        this.diagnosisDate = diagnosisDate;
        this.recoveryDate = recoveryDate;
        this.treatment = treatment;
        this.animal = animal;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDiagnosisDate() {
        return diagnosisDate;
    }

    public void setDiagnosisDate(LocalDate diagnosisDate) {
        this.diagnosisDate = diagnosisDate;
    }

    public LocalDate getRecoveryDate() {
        return recoveryDate;
    }

    public void setRecoveryDate(LocalDate recoveryDate) {
        this.recoveryDate = recoveryDate;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

}
