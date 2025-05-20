package com.example.demo.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.services.VaccineService;
import com.example.demo.util.SecurityUtils;

@RestController
@RequestMapping("/api/vaccines")
public class GlobalVaccineController {

    private final VaccineService vaccineService;

    public GlobalVaccineController(VaccineService vaccineService) {
        this.vaccineService = vaccineService;
    }

    /**
     * Get all animals with pending vaccines for the authenticated user
     * 
     * @param page Page number (0-based)
     * @param size Page size
     * @param sortBy Field to sort by (default: "name")
     * @param direction Sort direction (ASC or DESC)
     * @return Paginated list of animals with pending vaccines
     */
    @GetMapping("/pending-animals")
    public ResponseEntity<Map<String, Object>> getAnimalsWithPendingVaccines(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {
        
        // Get the current authenticated user's ID
        Long userId = SecurityUtils.getCurrentUserId();
        
        try {
            // Create pageable object
            Sort.Direction sortDirection = Sort.Direction.fromString(direction);
            Pageable pageable = PageRequest.of(page, size, sortDirection, sortBy);
            
            // Get paginated animals with pending vaccines
            Map<String, Object> animalsData = vaccineService.getAnimalsWithPendingVaccines(userId, pageable);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", animalsData);
            response.put("message", "Animals with pending vaccines successfully retrieved");
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
