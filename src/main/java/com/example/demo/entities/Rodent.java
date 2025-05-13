package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("RODENT")
public class Rodent extends Animal {

    private String species;

    @Column(name = "cage_trained")
    private boolean cageTrained;

    @Column(name = "lifespan_years")
    private Integer lifespanYears;

    @Column(name = "teeth_condition")
    private String teethCondition;

    protected Rodent() {
        super();
    }

    public Rodent(String name, LocalDate birthDate, Double weightKg, String color,
            String gender, boolean neutered, String species, boolean cageTrained) {
        super(name, birthDate, weightKg, color, gender, neutered);
        this.species = species;
        this.cageTrained = cageTrained;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public boolean isCageTrained() {
        return cageTrained;
    }

    public void setCageTrained(boolean cageTrained) {
        this.cageTrained = cageTrained;
    }

    public Integer getLifespanYears() {
        return lifespanYears;
    }

    public void setLifespanYears(Integer lifespanYears) {
        this.lifespanYears = lifespanYears;
    }

    public String getTeethCondition() {
        return teethCondition;
    }

    public void setTeethCondition(String teethCondition) {
        this.teethCondition = teethCondition;
    }
}
