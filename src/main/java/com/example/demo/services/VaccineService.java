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
import com.example.demo.dto.DetailLevel;
import com.example.demo.dto.VaccineDTO;
import com.example.demo.dto.VaccineRequestDTO;
import com.example.demo.entities.Animal;
import com.example.demo.entities.Vaccine;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repositories.AnimalRepository;
import com.example.demo.repositories.VaccineRepository;

@Service
public class VaccineService {

    private final VaccineRepository vaccineRepository;
    private final AnimalRepository animalRepository;

    public VaccineService(VaccineRepository vaccineRepository, AnimalRepository animalRepository) {
        this.vaccineRepository = vaccineRepository;
        this.animalRepository = animalRepository;
    }

    /**
     * Get all vaccines for an animal with pagination and optional date filtering
     * 
     * @param animalId      Animal ID
     * @param userId        User ID (for ownership validation)
     * @param startDate     Optional start date for filtering
     * @param endDate       Optional end date for filtering
     * @param dateType      Type of date to filter by ("application" or "expiration")
     * @param pageable      Pagination information
     * @return Map containing paginated list of vaccines and pagination metadata
     */
    public Map<String, Object> getVaccinesByAnimalId(
            Long animalId,
            Long userId,
            LocalDate startDate,
            LocalDate endDate,
            String dateType,
            Pageable pageable) {

        // First verify that the animal exists and belongs to the user
        if (!animalRepository.existsByIdAndUserId(animalId, userId)) {
            throw new ResourceNotFoundException("Animal", "id", animalId);
        }

        Page<Vaccine> vaccinePage;

        // Filter by date range if provided
        if (startDate != null && endDate != null) {
            if ("expiration".equalsIgnoreCase(dateType)) {
                // Filter by expiration date
                vaccinePage = vaccineRepository.findByAnimalIdAndUserIdAndExpirationDateBetween(
                        animalId, userId, startDate, endDate, pageable);
            } else {
                // Default: filter by application date
                vaccinePage = vaccineRepository.findByAnimalIdAndUserIdAndApplicationDateBetween(
                        animalId, userId, startDate, endDate, pageable);
            }
        } else {
            // No date filtering
            vaccinePage = vaccineRepository.findByAnimalIdAndUserId(animalId, userId, pageable);
        }

        // Convert entities to DTOs
        List<VaccineDTO> vaccines = vaccinePage.getContent().stream()
                .map(VaccineDTO::fromEntity)
                .collect(Collectors.toList());

        // Create response with pagination metadata
        Map<String, Object> response = new HashMap<>();
        response.put("vaccines", vaccines);
        response.put("currentPage", vaccinePage.getNumber());
        response.put("totalItems", vaccinePage.getTotalElements());
        response.put("totalPages", vaccinePage.getTotalPages());

        return response;
    }

    /**
     * Get a specific vaccine by ID
     * 
     * @param vaccineId Vaccine ID
     * @param animalId Animal ID
     * @param userId User ID (for ownership validation)
     * @return Vaccine DTO
     */
    public VaccineDTO getVaccineById(Long vaccineId, Long animalId, Long userId) {
        // Verify animal ownership
        if (!animalRepository.existsByIdAndUserId(animalId, userId)) {
            throw new ResourceNotFoundException("Animal", "id", animalId);
        }
        
        // Find vaccine by ID and animal ID
        return vaccineRepository.findByIdAndAnimalId(vaccineId, animalId)
                .map(VaccineDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Vaccine", "id", vaccineId));
    }
    
    /**
     * Create a new vaccine for an animal
     * 
     * @param request Vaccine data
     * @param animalId Animal ID
     * @param userId User ID (for ownership validation)
     * @return Created vaccine DTO
     */
    @Transactional
    public VaccineDTO createVaccine(VaccineRequestDTO request, Long animalId, Long userId) {
        request.validate();
        
        // Get animal and verify ownership
        Animal animal = animalRepository.findByIdAndUserId(animalId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Animal", "id", animalId));

        // When registering, applicationDate should be null, and expirationDate is required
        Vaccine vaccine = new Vaccine(
            request.getName(),
            null, // applicationDate is always null for new vaccines
            request.getExpirationDate(),
            request.getDescription()
        );
        vaccine.setAnimal(animal);
        
        Vaccine savedVaccine = vaccineRepository.save(vaccine);
        
        return VaccineDTO.fromEntity(savedVaccine);
    }
    
    /**
     * Update an existing vaccine
     * 
     * @param request Updated vaccine data
     * @param vaccineId Vaccine ID
     * @param animalId Animal ID
     * @param userId User ID (for ownership validation)
     * @return Updated vaccine DTO
     */
    @Transactional
    public VaccineDTO updateVaccine(VaccineRequestDTO request, Long vaccineId, Long animalId, Long userId) {
        request.setUpdate(true);
        request.validate();
        
        // Verify animal ownership
        if (!animalRepository.existsByIdAndUserId(animalId, userId)) {
            throw new ResourceNotFoundException("Animal", "id", animalId);
        }
        
        // Find vaccine by ID and animal ID
        Vaccine vaccine = vaccineRepository.findByIdAndAnimalId(vaccineId, animalId)
                .orElseThrow(() -> new ResourceNotFoundException("Vaccine", "id", vaccineId));
        
        // Update vaccine fields
        vaccine.setName(request.getName());
        // Always set applicationDate to null for regular updates
        // Application date can only be set via the dedicated endpoint
        vaccine.setApplicationDate(null);
        vaccine.setExpirationDate(request.getExpirationDate());
        vaccine.setDescription(request.getDescription());
        
        Vaccine updatedVaccine = vaccineRepository.save(vaccine);
        
        return VaccineDTO.fromEntity(updatedVaccine);
    }
    /**
     * Confirm vaccine application
     * This is the only endpoint that can set applicationDate
     * When a vaccine is applied, applicationDate is set to current date and expirationDate is cleared
     * 
     * @param vaccineId Vaccine ID to apply
     * @param animalId Animal ID
     * @param userId User ID (for ownership validation)
     * @return Updated vaccine DTO with application date set and expiration date cleared
     */
    @Transactional
    public VaccineDTO confirmVaccineApplication(Long vaccineId, Long animalId, Long userId) {
        // Verify animal ownership
        if (!animalRepository.existsByIdAndUserId(animalId, userId)) {
            throw new ResourceNotFoundException("Animal", "id", animalId);
        }
        
        // Find vaccine by ID and animal ID
        Vaccine vaccine = vaccineRepository.findByIdAndAnimalId(vaccineId, animalId)
                .orElseThrow(() -> new ResourceNotFoundException("Vaccine", "id", vaccineId));
        
        // Check if vaccine is already applied
        if (vaccine.getApplicationDate() != null) {
            throw new IllegalStateException("Vaccine is already applied on " + vaccine.getApplicationDate());
        }
        
        // Apply the vaccine - this sets applicationDate to today and expirationDate to null
        vaccine.applyVaccine();
        
        Vaccine updatedVaccine = vaccineRepository.save(vaccine);
        
        return VaccineDTO.fromEntity(updatedVaccine);
    }
    
    /**
     * Delete a vaccine
     * 
     * @param vaccineId Vaccine ID
     * @param animalId Animal ID
     * @param userId User ID (for ownership validation)
     */
    @Transactional
    public void deleteVaccine(Long vaccineId, Long animalId, Long userId) {
        // Verify animal ownership
        if (!animalRepository.existsByIdAndUserId(animalId, userId)) {
            throw new ResourceNotFoundException("Animal", "id", animalId);
        }
        
        // Find vaccine by ID and animal ID
        Vaccine vaccine = vaccineRepository.findByIdAndAnimalId(vaccineId, animalId)
                .orElseThrow(() -> new ResourceNotFoundException("Vaccine", "id", vaccineId));
        
        // Delete vaccine
        vaccineRepository.delete(vaccine);
    }
    
    /**
     * Get vaccines with future expiration dates (not yet expired)
     * 
     * @param animalId Animal ID
     * @param userId User ID (for ownership validation)
     * @param pageable Pagination information
     * @return Map containing paginated list of non-expired vaccines and pagination metadata
     */
    public Map<String, Object> getNonExpiredVaccines(Long animalId, Long userId, Pageable pageable) {
        // Verify that the animal exists and belongs to the user
        if (!animalRepository.existsByIdAndUserId(animalId, userId)) {
            throw new ResourceNotFoundException("Animal", "id", animalId);
        }

        // Get current date to find vaccines that have not yet expired
        LocalDate currentDate = LocalDate.now();
        
        // Get paginated non-expired vaccines
        Page<Vaccine> vaccinePage = vaccineRepository.findNonExpiredVaccinesByAnimalIdAndUserId(
                animalId, userId, currentDate, pageable);

        // Convert entities to DTOs
        List<VaccineDTO> vaccines = vaccinePage.getContent().stream()
                .map(VaccineDTO::fromEntity)
                .collect(Collectors.toList());

        // Create response with pagination metadata
        Map<String, Object> response = new HashMap<>();
        response.put("vaccines", vaccines);
        response.put("currentPage", vaccinePage.getNumber());
        response.put("totalItems", vaccinePage.getTotalElements());
        response.put("totalPages", vaccinePage.getTotalPages());

        return response;
    }
    
    /**
     * Get confirmed vaccines (vaccines with application date)
     * 
     * @param animalId Animal ID
     * @param userId User ID (for ownership validation)
     * @param pageable Pagination information
     * @return Map containing paginated list of confirmed vaccines and pagination metadata
     */
    public Map<String, Object> getConfirmedVaccines(Long animalId, Long userId, Pageable pageable) {
        // Verify that the animal exists and belongs to the user
        if (!animalRepository.existsByIdAndUserId(animalId, userId)) {
            throw new ResourceNotFoundException("Animal", "id", animalId);
        }
        
        // Get paginated confirmed vaccines
        Page<Vaccine> vaccinePage = vaccineRepository.findConfirmedVaccinesByAnimalIdAndUserId(
                animalId, userId, pageable);

        // Convert entities to DTOs
        List<VaccineDTO> vaccines = vaccinePage.getContent().stream()
                .map(VaccineDTO::fromEntity)
                .collect(Collectors.toList());        // Create response with pagination metadata
        Map<String, Object> response = new HashMap<>();
        response.put("vaccines", vaccines);
        response.put("currentPage", vaccinePage.getNumber());
        response.put("totalItems", vaccinePage.getTotalElements());
        response.put("totalPages", vaccinePage.getTotalPages());

        return response;
    }
    
    /**
     * Get all animals with pending vaccines for a user
     * 
     * @param userId User ID
     * @param pageable Pagination information
     * @return Map containing paginated list of animals with pending vaccines and pagination metadata
     */
    public Map<String, Object> getAnimalsWithPendingVaccines(Long userId, Pageable pageable) {
        // Get current date
        LocalDate currentDate = LocalDate.now();
        
        // Get paginated animals with pending vaccines
        Page<Animal> animalPage = vaccineRepository.findAnimalsWithPendingVaccines(
                userId, currentDate, pageable);
        
        // Convert entities to DTOs
        List<AnimalDTO> animals = animalPage.getContent().stream()
                .map(animal -> AnimalDTO.fromEntity(animal, DetailLevel.BASIC))
                .collect(Collectors.toList());
        
        // Create response with pagination metadata
        Map<String, Object> response = new HashMap<>();
        response.put("animals", animals);
        response.put("currentPage", animalPage.getNumber());
        response.put("totalItems", animalPage.getTotalElements());
        response.put("totalPages", animalPage.getTotalPages());
        
        return response;
    }
}
