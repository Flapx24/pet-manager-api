package com.example.demo.controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
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
     * Get all animals for the authenticated user with optional filtering,
     * pagination and sorting
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllAnimals(
            @RequestParam(defaultValue = "BASIC") String detailLevel,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String animalType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam(defaultValue = "false") boolean paginated) {

        Long userId = SecurityUtils.getCurrentUserId();
        DetailLevel level = DetailLevel.valueOf(detailLevel.toUpperCase());

        try {

            if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Start date cannot be after end date");
            }

            Map<String, Object> response = new HashMap<>();

            if (paginated || name != null || animalType != null || startDate != null || endDate != null) {

                Sort.Direction sortDirection = Sort.Direction.fromString(direction);
                Pageable pageable = PageRequest.of(page, size, sortDirection, sortBy);

                Map<String, Object> animalsData = animalService.getAllAnimalsByUserIdWithFilters(
                        userId, name, animalType, startDate, endDate, level, pageable);

                response.put("success", true);
                response.put("data", animalsData);
                response.put("message", "Animales recuperados correctamente");
            } else {

                List<AnimalDTO> animals = animalService.getAllAnimalsByUserId(userId, level);

                response.put("success", true);
                response.put("data", animals);
                response.put("message", "Animales recuperados correctamente");
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
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
    
    /**
     * Get all animals with pending vaccines
     * Pending vaccines are those that have an expiration date in the future but haven't been applied yet
     * 
     * @param detailLevel Detail level for animal information (BASIC or FULL)
     * @param page Page number (0-based)
     * @param size Page size
     * @param sortBy Field to sort by (default: name)
     * @param direction Sort direction (ASC or DESC)
     * @return Paginated list of animals with pending vaccines
     */
    @GetMapping("/with-pending-vaccines")
    public ResponseEntity<Map<String, Object>> getAnimalsWithPendingVaccines(
            @RequestParam(defaultValue = "BASIC") String detailLevel,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {
            
        Long userId = SecurityUtils.getCurrentUserId();
        DetailLevel level = DetailLevel.valueOf(detailLevel.toUpperCase());
        
        try {
            // Create pageable object
            Sort.Direction sortDirection = Sort.Direction.fromString(direction);
            Pageable pageable = PageRequest.of(page, size, sortDirection, sortBy);
            
            // Get paginated animals with pending vaccines
            Map<String, Object> animalsData = animalService.getAnimalsWithPendingVaccines(userId, level, pageable);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", animalsData);
            response.put("message", "Animales con vacunas pendientes recuperados correctamente");
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
