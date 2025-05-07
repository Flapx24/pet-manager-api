package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("CAT")
public class Cat extends Animal {

    private String breed;
    
    @Column(name = "coat_type")
    private String coatType;
    
    @Column(name = "indoor_only")
    private boolean indoorOnly;

    protected Cat() {
        super();
    }

    public Cat(String name, LocalDate birthDate, Double weightKg, String color, 
              String gender, boolean neutered, String breed, String coatType, boolean indoorOnly) {
        super(name, birthDate, weightKg, color, gender, neutered);
        this.breed = breed;
        this.coatType = coatType;
        this.indoorOnly = indoorOnly;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getCoatType() {
        return coatType;
    }

    public void setCoatType(String coatType) {
        this.coatType = coatType;
    }

    public boolean isIndoorOnly() {
        return indoorOnly;
    }

    public void setIndoorOnly(boolean indoorOnly) {
        this.indoorOnly = indoorOnly;
    }
}