package com.example.demo.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

/**
 * DTO for requests to create/update health issues
 */
public class HealthIssueRequestDTO {

    @NotBlank(message = "El nombre del problema de salud es requerido")
    private String name;

    @NotNull(message = "La fecha de diagnóstico es requerida")
    @PastOrPresent(message = "La fecha de diagnóstico no puede estar en el futuro")
    private LocalDate diagnosisDate;

    private LocalDate recoveryDate;

    private String description;

    private String treatment;

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
}
