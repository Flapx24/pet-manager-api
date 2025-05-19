package com.example.demo.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.FutureOrPresent;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VaccineRequestDTO {
    @NotBlank(message = "Vaccine name is required")
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate applicationDate;
    
    @FutureOrPresent(message = "Expiration date must be today or in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;
    
    private String description;
    
    // Field to differentiate between create and update operations
    private boolean isUpdate = false;

    public VaccineRequestDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isUpdate() {
        return isUpdate;
    }
    
    public void setUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }
    
    /**
     * Validates that the DTO follows business rules:
     * - For creation: applicationDate must be null and expirationDate must be set
     * - For updates: either applicationDate or expirationDate must be null, but not both
     */    public void validate() {
        // Application date should always be ignored for both create and regular update operations
        // Application date can only be set via the confirmVaccineApplication endpoint
        if (applicationDate != null) {
            throw new IllegalArgumentException("Application date cannot be set directly. Use the application confirmation endpoint instead.");
        }
        
        if (!isUpdate) {
            // Create scenario - expiration date is required
            if (expirationDate == null) {
                throw new IllegalArgumentException("Expiration date is required when registering a new vaccine");
            }
        }
    }
}
