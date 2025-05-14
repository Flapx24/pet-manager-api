package com.example.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.HealthIssueDTO;
import com.example.demo.dto.HealthIssueRequestDTO;
import com.example.demo.entities.Animal;
import com.example.demo.entities.HealthIssue;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repositories.AnimalRepository;
import com.example.demo.repositories.HealthIssueRepository;

@Service
public class HealthIssueService {

    private final HealthIssueRepository healthIssueRepository;
    private final AnimalRepository animalRepository;

    public HealthIssueService(HealthIssueRepository healthIssueRepository, AnimalRepository animalRepository) {
        this.healthIssueRepository = healthIssueRepository;
        this.animalRepository = animalRepository;
    }

    public List<HealthIssueDTO> getAllHealthIssuesByAnimalId(Long animalId, Long userId) {

        if (!animalRepository.existsByIdAndUserId(animalId, userId)) {
            throw new ResourceNotFoundException("Animal", "id", animalId);
        }

        return healthIssueRepository.findByAnimalId(animalId).stream()
                .map(HealthIssueDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public HealthIssueDTO getHealthIssueById(Long healthIssueId, Long animalId, Long userId) {

        if (!animalRepository.existsByIdAndUserId(animalId, userId)) {
            throw new ResourceNotFoundException("Animal", "id", animalId);
        }

        HealthIssue healthIssue = healthIssueRepository.findByIdAndAnimalId(healthIssueId, animalId)
                .orElseThrow(() -> new ResourceNotFoundException("Problema de salud", "id", healthIssueId));

        return HealthIssueDTO.fromEntity(healthIssue);
    }

    @Transactional
    public HealthIssueDTO createHealthIssue(HealthIssueRequestDTO request, Long animalId, Long userId) {
        Animal animal = animalRepository.findByIdAndUserId(animalId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Animal", "id", animalId));

        HealthIssue healthIssue = new HealthIssue(request.getName(), request.getDescription(),
                request.getDiagnosisDate(), request.getRecoveryDate(), request.getTreatment(), animal);

        healthIssue.setTreatment(request.getTreatment());

        healthIssue.setAnimal(animal);
        animal.addHealthIssue(healthIssue);

        HealthIssue savedHealthIssue = healthIssueRepository.save(healthIssue);
        return HealthIssueDTO.fromEntity(savedHealthIssue);
    }

    @Transactional
    public HealthIssueDTO updateHealthIssue(HealthIssueRequestDTO request, Long healthIssueId, Long animalId,
            Long userId) {

        if (!animalRepository.existsByIdAndUserId(animalId, userId)) {
            throw new ResourceNotFoundException("Animal", "id", animalId);
        }

        HealthIssue healthIssue = healthIssueRepository.findByIdAndAnimalId(healthIssueId, animalId)
                .orElseThrow(() -> new ResourceNotFoundException("Problema de salud", "id", healthIssueId));

        healthIssue.setName(request.getName());
        healthIssue.setDiagnosisDate(request.getDiagnosisDate());
        healthIssue.setRecoveryDate(request.getRecoveryDate());
        healthIssue.setDescription(request.getDescription());
        healthIssue.setTreatment(request.getTreatment());

        HealthIssue updatedHealthIssue = healthIssueRepository.save(healthIssue);
        return HealthIssueDTO.fromEntity(updatedHealthIssue);
    }

    @Transactional
    public void deleteHealthIssue(Long healthIssueId, Long animalId, Long userId) {

        if (!animalRepository.existsByIdAndUserId(animalId, userId)) {
            throw new ResourceNotFoundException("Animal", "id", animalId);
        }

        if (!healthIssueRepository.existsByIdAndAnimalId(healthIssueId, animalId)) {
            throw new ResourceNotFoundException("Problema de salud", "id", healthIssueId);
        }

        healthIssueRepository.deleteByIdAndAnimalId(healthIssueId, animalId);
    }
}
