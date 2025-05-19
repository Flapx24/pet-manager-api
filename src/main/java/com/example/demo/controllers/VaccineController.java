package com.example.demo.controllers;

import java.time.LocalDate;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.PatchMapping;

import com.example.demo.dto.VaccineDTO;
import com.example.demo.dto.VaccineRequestDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.services.VaccineService;
import com.example.demo.util.SecurityUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/animals/{animalId}/vaccines")
public class VaccineController {

    private final VaccineService vaccineService;

    public VaccineController(VaccineService vaccineService) {
        this.vaccineService = vaccineService;
    }

    /**
     * Get all vaccines for an animal with pagination and optional date filtering
     * 
     * @param animalId Animal ID
     * @param page Page number (0-based)
     * @param size Page size
     * @param sortBy Field to sort by (default: applicationDate)
     * @param direction Sort direction (ASC or DESC)
     * @param startDate Optional start date for filtering (format: yyyy-MM-dd)
     * @param endDate Optional end date for filtering (format: yyyy-MM-dd)
     * @param dateType Type of date to filter by (application or expiration)
     * @return Paginated list of vaccines
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllVaccines(
            @PathVariable Long animalId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "applicationDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "application") String dateType) {
        
        // Get the current authenticated user's ID
        Long userId = SecurityUtils.getCurrentUserId();
        
        try {
            // Validate date type
            if (!"application".equalsIgnoreCase(dateType) && !"expiration".equalsIgnoreCase(dateType)) {
                throw new IllegalArgumentException("Date type must be either 'application' or 'expiration'");
            }
            
            // Validate date range if both parameters are provided
            if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Start date cannot be after end date");
            }
            
            // If only one date is provided, set the other accordingly
            if (startDate != null && endDate == null) {
                endDate = LocalDate.now();
            } else if (startDate == null && endDate != null) {
                startDate = LocalDate.of(1900, 1, 1); // Arbitrary old date
            }
            
            // Create pageable object
            Sort.Direction sortDirection = Sort.Direction.fromString(direction);
            Pageable pageable = PageRequest.of(page, size, sortDirection, sortBy);
            
            // Get paginated vaccines with date type filtering
            Map<String, Object> vaccinesData = 
                vaccineService.getVaccinesByAnimalId(animalId, userId, startDate, endDate, dateType, pageable);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", vaccinesData);
            response.put("message", "Vaccine history successfully retrieved");
            
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
     * Get a specific vaccine by ID
     * 
     * @param animalId Animal ID
     * @param vaccineId Vaccine ID
     * @return Vaccine data
     */
    @GetMapping("/{vaccineId}")
    public ResponseEntity<Map<String, Object>> getVaccineById(
            @PathVariable Long animalId,
            @PathVariable Long vaccineId) {

        Long userId = SecurityUtils.getCurrentUserId();

        try {
            VaccineDTO vaccine = vaccineService.getVaccineById(vaccineId, animalId, userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", vaccine);
            response.put("message", "Vaccine successfully retrieved");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Create a new vaccine
     * 
     * @param animalId Animal ID
     * @param request Vaccine data
     * @return Created vaccine
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createVaccine(
            @PathVariable Long animalId,
            @Valid @RequestBody VaccineRequestDTO request) {

        Long userId = SecurityUtils.getCurrentUserId();

        try {
            VaccineDTO savedVaccine = vaccineService.createVaccine(request, animalId, userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", savedVaccine);
            response.put("message", "Vaccine successfully created");

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Update an existing vaccine
     * 
     * @param animalId Animal ID
     * @param vaccineId Vaccine ID
     * @param request Updated vaccine data
     * @return Updated vaccine
     */
    @PutMapping("/{vaccineId}")
    public ResponseEntity<Map<String, Object>> updateVaccine(
            @PathVariable Long animalId,
            @PathVariable Long vaccineId,
            @Valid @RequestBody VaccineRequestDTO request) {

        Long userId = SecurityUtils.getCurrentUserId();

        try {
            VaccineDTO updatedVaccine = vaccineService.updateVaccine(request, vaccineId, animalId, userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", updatedVaccine);
            response.put("message", "Vaccine successfully updated");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Confirm the application of a vaccine
     * This endpoint marks a vaccine as applied, setting the application date to now
     * and removing the expiration date
     * 
     * @param animalId Animal ID
     * @param vaccineId Vaccine ID
     * @return Applied vaccine information
     */
    @PatchMapping("/{vaccineId}/apply")
    public ResponseEntity<Map<String, Object>> confirmVaccineApplication(
            @PathVariable Long animalId,
            @PathVariable Long vaccineId) {

        Long userId = SecurityUtils.getCurrentUserId();

        try {
            VaccineDTO appliedVaccine = vaccineService.confirmVaccineApplication(vaccineId, animalId, userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", appliedVaccine);
            response.put("message", "Vaccine successfully applied");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delete a vaccine
     * 
     * @param animalId Animal ID
     * @param vaccineId Vaccine ID
     * @return Success message
     */
    @DeleteMapping("/{vaccineId}")
    public ResponseEntity<Map<String, Object>> deleteVaccine(
            @PathVariable Long animalId,
            @PathVariable Long vaccineId) {

        Long userId = SecurityUtils.getCurrentUserId();

        try {
            vaccineService.deleteVaccine(vaccineId, animalId, userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Vaccine successfully deleted");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
