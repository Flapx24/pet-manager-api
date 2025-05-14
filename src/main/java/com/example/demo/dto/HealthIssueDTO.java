package com.example.demo.dto;

import java.time.LocalDate;

import com.example.demo.entities.HealthIssue;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HealthIssueDTO {
    private Long id;
    private String name;
    private LocalDate diagnosisDate;
    private LocalDate recoveryDate;
    private String description;
    private String treatment;
    private Long animalId;

    public HealthIssueDTO() {
    }

    /**
     * Creates a DTO from a HealthIssue entity
     * 
     * @param healthIssue Health issue entity
     * @return DTO with health issue information
     */
    public static HealthIssueDTO fromEntity(HealthIssue healthIssue) {
        if (healthIssue == null) {
            return null;
        }

        HealthIssueDTO dto = new HealthIssueDTO();
        dto.id = healthIssue.getId();
        dto.name = healthIssue.getName();
        dto.diagnosisDate = healthIssue.getDiagnosisDate();
        dto.recoveryDate = healthIssue.getRecoveryDate();
        dto.description = healthIssue.getDescription();
        dto.treatment = healthIssue.getTreatment();

        if (healthIssue.getAnimal() != null) {
            dto.animalId = healthIssue.getAnimal().getId();
        }

        return dto;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }
}
