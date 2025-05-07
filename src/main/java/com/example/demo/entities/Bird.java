package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("BIRD")
public class Bird extends Animal {

    private String species;
    
    @Column(name = "clipped_wings")
    private boolean clippedWings;
    
    @Column(name = "talking_ability")
    private boolean talkingAbility;

    protected Bird() {
        super();
    }

    public Bird(String name, LocalDate birthDate, Double weightKg, String color, 
               String gender, boolean neutered, String species, boolean clippedWings) {
        super(name, birthDate, weightKg, color, gender, neutered);
        this.species = species;
        this.clippedWings = clippedWings;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public boolean isClippedWings() {
        return clippedWings;
    }

    public void setClippedWings(boolean clippedWings) {
        this.clippedWings = clippedWings;
    }

    public boolean isTalkingAbility() {
        return talkingAbility;
    }

    public void setTalkingAbility(boolean talkingAbility) {
        this.talkingAbility = talkingAbility;
    }
}