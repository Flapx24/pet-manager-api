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

import com.example.demo.dto.HealthIssueDTO;
import com.example.demo.dto.HealthIssueRequestDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.services.HealthIssueService;
import com.example.demo.util.SecurityUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/animals/{animalId}/health-issues")
public class HealthIssueController {

    private final HealthIssueService healthIssueService;

    public HealthIssueController(HealthIssueService healthIssueService) {
        this.healthIssueService = healthIssueService;
    }

    /**
     * Get all health issues for an animal with optional filtering, pagination and
     * sorting
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllHealthIssues(
            @PathVariable Long animalId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "diagnosisDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestParam(defaultValue = "false") boolean paginated) {

        Long userId = SecurityUtils.getCurrentUserId();

        try {

            if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Start date cannot be after end date");
            }

            Map<String, Object> response = new HashMap<>();

            if (paginated || name != null || startDate != null || endDate != null) {

                Sort.Direction sortDirection = Sort.Direction.fromString(direction);
                Pageable pageable = PageRequest.of(page, size, sortDirection, sortBy);

                Map<String, Object> healthIssuesData = healthIssueService.getAllHealthIssuesByAnimalIdWithFilters(
                        animalId, userId, name, startDate, endDate, pageable);

                response.put("success", true);
                response.put("data", healthIssuesData);
                response.put("message", "Problemas de salud recuperados correctamente");
            } else {

                List<HealthIssueDTO> healthIssues = healthIssueService.getAllHealthIssuesByAnimalId(animalId, userId);

                response.put("success", true);
                response.put("data", healthIssues);
                response.put("message", "Problemas de salud recuperados correctamente");
            }

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
     * Get a specific health issue by ID
     */
    @GetMapping("/{healthIssueId}")
    public ResponseEntity<Map<String, Object>> getHealthIssueById(
            @PathVariable Long animalId,
            @PathVariable Long healthIssueId) {

        Long userId = SecurityUtils.getCurrentUserId();

        try {
            HealthIssueDTO healthIssue = healthIssueService.getHealthIssueById(healthIssueId, animalId, userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", healthIssue);
            response.put("message", "Problema de salud recuperado correctamente");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Create a new health issue
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createHealthIssue(
            @PathVariable Long animalId,
            @Valid @RequestBody HealthIssueRequestDTO request) {

        Long userId = SecurityUtils.getCurrentUserId();

        try {
            HealthIssueDTO savedHealthIssue = healthIssueService.createHealthIssue(request, animalId, userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", savedHealthIssue);
            response.put("message", "Problema de salud creado correctamente");

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Update an existing health issue
     */
    @PutMapping("/{healthIssueId}")
    public ResponseEntity<Map<String, Object>> updateHealthIssue(
            @PathVariable Long animalId,
            @PathVariable Long healthIssueId,
            @Valid @RequestBody HealthIssueRequestDTO request) {

        Long userId = SecurityUtils.getCurrentUserId();

        try {
            HealthIssueDTO updatedHealthIssue = healthIssueService.updateHealthIssue(
                    request, healthIssueId, animalId, userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", updatedHealthIssue);
            response.put("message", "Problema de salud actualizado correctamente");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Delete a health issue
     */
    @DeleteMapping("/{healthIssueId}")
    public ResponseEntity<Map<String, Object>> deleteHealthIssue(
            @PathVariable Long animalId,
            @PathVariable Long healthIssueId) {

        Long userId = SecurityUtils.getCurrentUserId();

        try {
            healthIssueService.deleteHealthIssue(healthIssueId, animalId, userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Problema de salud eliminado correctamente");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
