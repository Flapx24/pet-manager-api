package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("REPTILE")
public class Reptile extends Animal {

    private String species;
    
    @Column(name = "habitat_type")
    private String habitatType; // desert, tropical, aquatic, etc.
    
    @Column(name = "temp_requirements")
    private String temperatureRequirements;
    
    private boolean venomous;

    protected Reptile() {
        super();
    }
    
    public Reptile(String name, LocalDate birthDate, Double weightKg, String color, 
                  String gender, boolean neutered, String species, String habitatType) {
        super(name, birthDate, weightKg, color, gender, neutered);
        this.species = species;
        this.habitatType = habitatType;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getHabitatType() {
        return habitatType;
    }

    public void setHabitatType(String habitatType) {
        this.habitatType = habitatType;
    }

    public String getTemperatureRequirements() {
        return temperatureRequirements;
    }

    public void setTemperatureRequirements(String temperatureRequirements) {
        this.temperatureRequirements = temperatureRequirements;
    }

    public boolean isVenomous() {
        return venomous;
    }

    public void setVenomous(boolean venomous) {
        this.venomous = venomous;
    }
}