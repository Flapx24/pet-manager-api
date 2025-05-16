package com.example.demo.services;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.AnimalDTO;
import com.example.demo.dto.AnimalRequestDTO;
import com.example.demo.dto.DetailLevel;
import com.example.demo.entities.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repositories.AnimalRepository;
import com.example.demo.repositories.UserRepository;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;

    public AnimalService(AnimalRepository animalRepository, UserRepository userRepository) {
        this.animalRepository = animalRepository;
        this.userRepository = userRepository;
    }

    /**
     * Get all animals for a specific user with basic filtering
     */
    public List<AnimalDTO> getAllAnimalsByUserId(Long userId, DetailLevel detailLevel) {
        return animalRepository.findByUserId(userId).stream()
                .map(animal -> AnimalDTO.fromEntity(animal, detailLevel))
                .collect(Collectors.toList());
    }

    /**
     * Get all animals for a specific user with advanced filtering, pagination and
     * sorting
     */
    public Map<String, Object> getAllAnimalsByUserIdWithFilters(
            Long userId,
            String name,
            String animalType,
            LocalDate startDate,
            LocalDate endDate,
            DetailLevel detailLevel,
            Pageable pageable) {

        Page<Animal> animalPage;

        if (name != null && animalType != null && startDate != null && endDate != null) {
            animalPage = animalRepository.findByUserIdAndNameContainingIgnoreCaseAndAnimalTypeAndBirthDateBetween(
                    userId, name, animalType, startDate, endDate, pageable);
        } else if (name != null && animalType != null) {
            animalPage = animalRepository.findByUserIdAndNameContainingIgnoreCaseAndAnimalType(
                    userId, name, animalType, pageable);
        } else if (name != null && startDate != null && endDate != null) {
            animalPage = animalRepository.findByUserIdAndNameContainingIgnoreCaseAndBirthDateBetween(
                    userId, name, startDate, endDate, pageable);
        } else if (animalType != null && startDate != null && endDate != null) {
            animalPage = animalRepository.findByUserIdAndAnimalTypeAndBirthDateBetween(
                    userId, animalType, startDate, endDate, pageable);
        } else if (name != null) {
            animalPage = animalRepository.findByUserIdAndNameContainingIgnoreCase(userId, name, pageable);
        } else if (animalType != null) {
            animalPage = animalRepository.findByUserIdAndAnimalType(userId, animalType, pageable);
        } else if (startDate != null && endDate != null) {
            animalPage = animalRepository.findByUserIdAndBirthDateBetween(userId, startDate, endDate, pageable);
        } else {
            animalPage = animalRepository.findByUserId(userId, pageable);
        }

        List<AnimalDTO> animals = animalPage.getContent().stream()
                .map(animal -> AnimalDTO.fromEntity(animal, detailLevel))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("animals", animals);
        response.put("currentPage", animalPage.getNumber());
        response.put("totalItems", animalPage.getTotalElements());
        response.put("totalPages", animalPage.getTotalPages());

        return response;
    }

    /**
     * Get a specific animal by ID, ensuring it belongs to the specified user
     */
    public AnimalDTO getAnimalById(Long animalId, Long userId, DetailLevel detailLevel) {
        Animal animal = animalRepository.findByIdAndUserId(animalId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Animal", "id", animalId));
        return AnimalDTO.fromEntity(animal, detailLevel);
    }

    /**
     * Create a new animal for a specific user
     */
    @Transactional
    public AnimalDTO createAnimal(AnimalRequestDTO request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Animal animal;

        switch (request.getAnimalType()) {
            case DOG -> {
                Dog dog = new Dog(
                        request.getName(),
                        request.getBirthDate(),
                        request.getWeightKg(),
                        request.getColor(),
                        request.getGender(),
                        request.isNeutered(),
                        request.getBreed(),
                        request.getSize(),
                        request.getCoatType());
                dog.setPedigree(request.isPedigree());
                animal = dog;
            }

            case CAT -> {
                Cat cat = new Cat(
                        request.getName(),
                        request.getBirthDate(),
                        request.getWeightKg(),
                        request.getColor(),
                        request.getGender(),
                        request.isNeutered(),
                        request.getBreed(),
                        request.getCoatType(),
                        request.isIndoorOnly());
                animal = cat;
            }

            case BIRD -> {
                Bird bird = new Bird(
                        request.getName(),
                        request.getBirthDate(),
                        request.getWeightKg(),
                        request.getColor(),
                        request.getGender(),
                        request.isNeutered(),
                        request.getSpecies(),
                        request.isClippedWings());
                bird.setTalkingAbility(request.isTalkingAbility());
                animal = bird;
            }

            case REPTILE -> {
                Reptile reptile = new Reptile(
                        request.getName(),
                        request.getBirthDate(),
                        request.getWeightKg(),
                        request.getColor(),
                        request.getGender(),
                        request.isNeutered(),
                        request.getSpecies(),
                        request.getHabitatType());
                reptile.setTemperatureRequirements(request.getTemperatureRequirements());
                reptile.setVenomous(request.isVenomous());
                animal = reptile;
            }

            case FISH -> {
                Fish fish = new Fish(
                        request.getName(),
                        request.getBirthDate(),
                        request.getWeightKg(),
                        request.getColor(),
                        request.getGender(),
                        request.isNeutered(),
                        request.getSpecies(),
                        request.getWaterType());
                fish.setWaterTemperature(request.getWaterTemperature());
                fish.setPhLevel(request.getPhLevel());
                fish.setSocialBehavior(request.getSocialBehavior());
                animal = fish;
            }

            case RODENT -> {
                Rodent rodent = new Rodent(
                        request.getName(),
                        request.getBirthDate(),
                        request.getWeightKg(),
                        request.getColor(),
                        request.getGender(),
                        request.isNeutered(),
                        request.getSpecies(),
                        request.isCageTrained());
                rodent.setLifespanYears(request.getLifespanYears());
                rodent.setTeethCondition(request.getTeethCondition());
                animal = rodent;
            }

            default -> throw new IllegalArgumentException("Tipo de animal no soportado: " + request.getAnimalType());
        }

        animal.setNotes(request.getNotes());
        animal.setDiet(request.getDiet());
        animal.setLastDeworming(request.getLastDeworming());

        animal.setUser(user);
        Animal savedAnimal = animalRepository.save(animal);
        return AnimalDTO.fromEntity(savedAnimal, DetailLevel.FULL);
    }

    /**
     * Update an existing animal
     */
    @Transactional
    public AnimalDTO updateAnimal(AnimalRequestDTO request, Long animalId, Long userId) {

        Animal existingAnimal = animalRepository.findByIdAndUserId(animalId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Animal", "id", animalId));

        if (existingAnimal.getAnimalType() != request.getAnimalType()) {
            throw new IllegalArgumentException("No se puede cambiar el tipo de animal");
        }

        existingAnimal.setName(request.getName());
        existingAnimal.setBirthDate(request.getBirthDate());
        existingAnimal.setWeightKg(request.getWeightKg());
        existingAnimal.setColor(request.getColor());
        existingAnimal.setGender(request.getGender());
        existingAnimal.setNeutered(request.isNeutered());
        existingAnimal.setNotes(request.getNotes());
        existingAnimal.setDiet(request.getDiet());
        existingAnimal.setLastDeworming(request.getLastDeworming());

        switch (existingAnimal.getAnimalType()) {
            case DOG -> {
                Dog dog = (Dog) existingAnimal;
                dog.setBreed(request.getBreed());
                dog.setSize(request.getSize());
                dog.setCoatType(request.getCoatType());
                dog.setPedigree(request.isPedigree());
            }

            case CAT -> {
                Cat cat = (Cat) existingAnimal;
                cat.setBreed(request.getBreed());
                cat.setCoatType(request.getCoatType());
                cat.setIndoorOnly(request.isIndoorOnly());
            }

            case BIRD -> {
                Bird bird = (Bird) existingAnimal;
                bird.setSpecies(request.getSpecies());
                bird.setClippedWings(request.isClippedWings());
                bird.setTalkingAbility(request.isTalkingAbility());
            }

            case REPTILE -> {
                Reptile reptile = (Reptile) existingAnimal;
                reptile.setSpecies(request.getSpecies());
                reptile.setHabitatType(request.getHabitatType());
                reptile.setTemperatureRequirements(request.getTemperatureRequirements());
                reptile.setVenomous(request.isVenomous());
            }

            case FISH -> {
                Fish fish = (Fish) existingAnimal;
                fish.setSpecies(request.getSpecies());
                fish.setWaterType(request.getWaterType());
                fish.setWaterTemperature(request.getWaterTemperature());
                fish.setPhLevel(request.getPhLevel());
                fish.setSocialBehavior(request.getSocialBehavior());
            }

            case RODENT -> {
                Rodent rodent = (Rodent) existingAnimal;
                rodent.setSpecies(request.getSpecies());
                rodent.setCageTrained(request.isCageTrained());
                rodent.setLifespanYears(request.getLifespanYears());
                rodent.setTeethCondition(request.getTeethCondition());
            }

            default ->
                throw new IllegalArgumentException("Tipo de animal no soportado: " + existingAnimal.getAnimalType());
        }

        Animal savedAnimal = animalRepository.save(existingAnimal);
        return AnimalDTO.fromEntity(savedAnimal, DetailLevel.FULL);
    }

    /**
     * Delete an animal
     */
    @Transactional
    public void deleteAnimal(Long animalId, Long userId) {

        if (!animalRepository.existsByIdAndUserId(animalId, userId)) {
            throw new ResourceNotFoundException("Animal", "id", animalId);
        }
        animalRepository.deleteById(animalId);
    }
}
