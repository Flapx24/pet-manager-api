package com.example.demo.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AnimalDTO;
import com.example.demo.dto.AnimalRequestDTO;
import com.example.demo.dto.DetailLevel;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.services.AnimalService;
import com.example.demo.util.SecurityUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/animals")
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    /**
     * Get all animals for the authenticated user
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllAnimals(
            @RequestParam(defaultValue = "BASIC") String detailLevel) {

        Long userId = SecurityUtils.getCurrentUserId();
        DetailLevel level = DetailLevel.valueOf(detailLevel.toUpperCase());
        List<AnimalDTO> animals = animalService.getAllAnimalsByUserId(userId, level);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", animals);
        response.put("message", "Animales recuperados correctamente");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get a specific animal by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getAnimalById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "FULL") String detailLevel) {

        Long userId = SecurityUtils.getCurrentUserId();
        DetailLevel level = DetailLevel.valueOf(detailLevel.toUpperCase());

        try {
            AnimalDTO animal = animalService.getAnimalById(id, userId, level);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", animal);
            response.put("message", "Animal recuperado correctamente");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Create a new animal
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createAnimal(@Valid @RequestBody AnimalRequestDTO request) {

        Long userId = SecurityUtils.getCurrentUserId();

        try {
            AnimalDTO savedAnimal = animalService.createAnimal(request, userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", savedAnimal);
            response.put("message", "Animal creado correctamente");

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Update an existing animal
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateAnimal(
            @PathVariable Long id,
            @Valid @RequestBody AnimalRequestDTO request) {

        Long userId = SecurityUtils.getCurrentUserId();

        try {
            AnimalDTO updatedAnimal = animalService.updateAnimal(request, id, userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", updatedAnimal);
            response.put("message", "Animal actualizado correctamente");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delete an animal
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteAnimal(@PathVariable Long id) {

        Long userId = SecurityUtils.getCurrentUserId();

        try {
            animalService.deleteAnimal(id, userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Animal eliminado correctamente");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
