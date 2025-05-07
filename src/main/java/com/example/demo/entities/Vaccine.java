package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

@Entity
@Table(name = "vaccines")
public class Vaccine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Vaccine name is required")
    private String name;

    @NotNull(message = "Vaccine date is required")
    @PastOrPresent(message = "Vaccine date cannot be in the future")
    @Column(name = "application_date")
    private LocalDate applicationDate;
    
    @Column(name = "expiration_date")
    private LocalDate expirationDate;
    
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;

    protected Vaccine() {
    }

    public Vaccine(String name, LocalDate applicationDate, LocalDate expirationDate, String description) {
        this.name = name;
        this.applicationDate = applicationDate;
        this.expirationDate = expirationDate;
        this.description = description;
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

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }
}