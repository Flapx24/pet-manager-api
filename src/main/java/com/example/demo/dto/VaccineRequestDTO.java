package com.example.demo.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VaccineRequestDTO {
    @NotBlank(message = "Vaccine name is required")
    private String name;

    @NotNull(message = "Application date is required")
    @PastOrPresent(message = "Application date cannot be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate applicationDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;
    
    private String description;

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
}
