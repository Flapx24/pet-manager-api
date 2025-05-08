package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.example.demo.enums.AnimalType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "animal_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "animals")
public abstract class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Birth date is required")
    @PastOrPresent(message = "Birth date cannot be in the future")
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
    @Column(name = "registration_date")
    private LocalDate registrationDate;
    
    @Positive(message = "Weight must be a positive value")
    @Column(name = "weight_kg")
    private Double weightKg;
    
    private String color;
    
    @Column(length = 1000)
    private String notes;
    
    private String diet;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "animal_type", insertable = false, updatable = false)
    private AnimalType animalType;
    
    @NotNull(message = "Gender is required")
    private String gender;
    
    private boolean neutered;
    
    @Column(name = "last_deworming")
    private LocalDate lastDeworming;
    
    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Vaccine> vaccines = new HashSet<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    protected Animal() {
    }

    protected Animal(String name, LocalDate birthDate, Double weightKg, String color, 
                  String gender, boolean neutered) {
        this.name = name;
        this.birthDate = birthDate;
        this.registrationDate = LocalDate.now();
        this.weightKg = weightKg;
        this.color = color;
        this.gender = gender;
        this.neutered = neutered;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isNeutered() {
        return neutered;
    }

    public void setNeutered(boolean neutered) {
        this.neutered = neutered;
    }
    
    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }
    
    public LocalDate getLastDeworming() {
        return lastDeworming;
    }

    public void setLastDeworming(LocalDate lastDeworming) {
        this.lastDeworming = lastDeworming;
    }
    
    public Set<Vaccine> getVaccines() {
        return vaccines;
    }
    
    public void addVaccine(Vaccine vaccine) {
        vaccines.add(vaccine);
        vaccine.setAnimal(this);
    }
    
    public void removeVaccine(Vaccine vaccine) {
        vaccines.remove(vaccine);
        vaccine.setAnimal(null);
    }
    
    public int getAgeYears() {
        return birthDate != null 
            ? LocalDate.now().getYear() - birthDate.getYear() 
            : 0;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}