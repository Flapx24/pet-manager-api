package com.example.demo.dto;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.example.demo.entities.*;
import com.example.demo.enums.AnimalType;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * DTO for Animal entities that can display different detail levels.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnimalDTO {
    // Basic common fields
    private Long id;
    private String name;
    private LocalDate birthDate;
    private LocalDate registrationDate;
    private Double weightKg;
    private String color;
    private String gender;
    private AnimalType animalType;
    
    // Additional fields for FULL level
    private String notes;
    private String diet;
    private boolean neutered;
    private LocalDate lastDeworming;
    private UserDTO owner;
    
    // Specific fields by animal type
    private Map<String, Object> specificFields;
    
    // Empty constructor
    public AnimalDTO() {
        this.specificFields = new HashMap<>();
    }
    
    /**
     * Creates a DTO from an Animal entity with the specified detail level
     * 
     * @param animal Animal entity
     * @param detailLevel Detail level (BASIC or FULL)
     * @return DTO with information according to the detail level
     */
    public static AnimalDTO fromEntity(Animal animal, DetailLevel detailLevel) {
        if (animal == null) {
            return null;
        }
        
        AnimalDTO dto = new AnimalDTO();
        
        // Basic fields always included
        dto.id = animal.getId();
        dto.name = animal.getName();
        dto.birthDate = animal.getBirthDate();
        dto.registrationDate = animal.getRegistrationDate();
        dto.weightKg = animal.getWeightKg();
        dto.color = animal.getColor();
        dto.gender = animal.getGender();
        dto.animalType = animal.getAnimalType();
        
        // Additional fields for FULL level
        if (detailLevel == DetailLevel.FULL) {
            dto.notes = animal.getNotes();
            dto.diet = animal.getDiet();
            dto.neutered = animal.isNeutered();
            dto.lastDeworming = animal.getLastDeworming();
            
            if (animal.getUser() != null) {
                dto.owner = UserDTO.fromEntity(animal.getUser());
            }
              // Specific fields based on animal type
            switch (animal.getAnimalType()) {
                case DOG -> {
                    Dog dog = (Dog) animal;
                    dto.specificFields.put("breed", dog.getBreed());
                    dto.specificFields.put("size", dog.getSize());
                    dto.specificFields.put("coatType", dog.getCoatType());
                    dto.specificFields.put("pedigree", dog.isPedigree());
                }
                
                case CAT -> {
                    Cat cat = (Cat) animal;
                    dto.specificFields.put("breed", cat.getBreed());
                    dto.specificFields.put("coatType", cat.getCoatType());
                    dto.specificFields.put("indoorOnly", cat.isIndoorOnly());
                }
                
                case BIRD -> {
                    Bird bird = (Bird) animal;
                    dto.specificFields.put("species", bird.getSpecies());
                    dto.specificFields.put("clippedWings", bird.isClippedWings());
                    dto.specificFields.put("talkingAbility", bird.isTalkingAbility());
                }
                
                case REPTILE -> {
                    Reptile reptile = (Reptile) animal;
                    dto.specificFields.put("species", reptile.getSpecies());
                    dto.specificFields.put("habitatType", reptile.getHabitatType());
                    dto.specificFields.put("temperatureRequirements", reptile.getTemperatureRequirements());
                    dto.specificFields.put("venomous", reptile.isVenomous());
                }
                
                case FISH -> {
                    Fish fish = (Fish) animal;
                    dto.specificFields.put("species", fish.getSpecies());
                    dto.specificFields.put("waterType", fish.getWaterType());
                    dto.specificFields.put("waterTemperature", fish.getWaterTemperature());
                    dto.specificFields.put("phLevel", fish.getPhLevel());
                    dto.specificFields.put("socialBehavior", fish.getSocialBehavior());
                }
                
                case RODENT -> {
                    Rodent rodent = (Rodent) animal;
                    dto.specificFields.put("species", rodent.getSpecies());
                    dto.specificFields.put("cageTrained", rodent.isCageTrained());
                    dto.specificFields.put("lifespanYears", rodent.getLifespanYears());
                    dto.specificFields.put("teethCondition", rodent.getTeethCondition());
                }
                
                default -> { /* No hacer nada para otros tipos */ }
            }
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg = weightKg;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    public void setAnimalType(AnimalType animalType) {
        this.animalType = animalType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public boolean isNeutered() {
        return neutered;
    }

    public void setNeutered(boolean neutered) {
        this.neutered = neutered;
    }

    public LocalDate getLastDeworming() {
        return lastDeworming;
    }

    public void setLastDeworming(LocalDate lastDeworming) {
        this.lastDeworming = lastDeworming;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public Map<String, Object> getSpecificFields() {
        return specificFields;
    }

    public void setSpecificFields(Map<String, Object> specificFields) {
        this.specificFields = specificFields;
    }
}