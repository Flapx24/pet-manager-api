package com.example.demo.dto;

import java.time.LocalDate;

import com.example.demo.entities.Vaccine;

public class VaccineDTO {
    private Long id;
    private String name;
    private LocalDate applicationDate;
    private LocalDate expirationDate;
    private String description;
    private Long animalId;

    public VaccineDTO() {
    }
    
    /**
     * Creates a DTO from a Vaccine entity
     * 
     * @param vaccine Vaccine entity
     * @return DTO with vaccine information
     */
    public static VaccineDTO fromEntity(Vaccine vaccine) {
        if (vaccine == null) {
            return null;
        }

        VaccineDTO dto = new VaccineDTO();
        dto.id = vaccine.getId();
        dto.name = vaccine.getName();
        dto.applicationDate = vaccine.getApplicationDate();
        dto.expirationDate = vaccine.getExpirationDate();
        dto.description = vaccine.getDescription();

        if (vaccine.getAnimal() != null) {
            dto.animalId = vaccine.getAnimal().getId();
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

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }
}
