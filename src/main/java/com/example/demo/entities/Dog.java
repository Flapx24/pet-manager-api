package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("DOG")
public class Dog extends Animal {

    private String breed;
    
    @Column(name = "size")
    private String size; // small, medium, large, giant
    
    @Column(name = "coat_type")
    private String coatType; // short, medium, long, curly, wire, double, etc.
    
    private boolean pedigree;

    protected Dog() {
        super();
    }

    public Dog(String name, LocalDate birthDate, Double weightKg, String color, 
              String gender, boolean neutered, String breed, String size, String coatType) {
        super(name, birthDate, weightKg, color, gender, neutered);
        this.breed = breed;
        this.size = size;
        this.coatType = coatType;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
    
    public String getCoatType() {
        return coatType;
    }

    public void setCoatType(String coatType) {
        this.coatType = coatType;
    }

    public boolean isPedigree() {
        return pedigree;
    }

    public void setPedigree(boolean pedigree) {
        this.pedigree = pedigree;
    }
}