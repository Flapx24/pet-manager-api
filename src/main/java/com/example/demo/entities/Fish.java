package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("FISH")
public class Fish extends Animal {

    private String species;
    
    @Column(name = "water_type")
    private String waterType; // freshwater, saltwater, brackish
    
    @Column(name = "water_temperature")
    private Double waterTemperature;
    
    @Column(name = "ph_level")
    private Double phLevel;
    
    @Column(name = "social_behavior")
    private String socialBehavior; // solitary, pairs, school, community

    protected Fish() {
        super();
    }

    public Fish(String name, LocalDate birthDate, Double weightKg, String color, 
               String gender, boolean neutered, String species, String waterType) {
        super(name, birthDate, weightKg, color, gender, neutered);
        this.species = species;
        this.waterType = waterType;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getWaterType() {
        return waterType;
    }

    public void setWaterType(String waterType) {
        this.waterType = waterType;
    }

    public Double getWaterTemperature() {
        return waterTemperature;
    }

    public void setWaterTemperature(Double waterTemperature) {
        this.waterTemperature = waterTemperature;
    }

    public Double getPhLevel() {
        return phLevel;
    }

    public void setPhLevel(Double phLevel) {
        this.phLevel = phLevel;
    }

    public String getSocialBehavior() {
        return socialBehavior;
    }

    public void setSocialBehavior(String socialBehavior) {
        this.socialBehavior = socialBehavior;
    }
}