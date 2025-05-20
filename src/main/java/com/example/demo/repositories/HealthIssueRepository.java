package com.example.demo.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.HealthIssue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HealthIssueRepository extends JpaRepository<HealthIssue, Long> {

        List<HealthIssue> findByAnimalId(Long animalId);

        Page<HealthIssue> findByAnimalId(Long animalId, Pageable pageable);

        @Query("SELECT h FROM HealthIssue h WHERE h.animal.id = :animalId AND LOWER(h.name) LIKE LOWER(CONCAT('%', :name, '%'))")
        Page<HealthIssue> findByAnimalIdAndNameContainingIgnoreCase(
                        @Param("animalId") Long animalId,
                        @Param("name") String name,
                        Pageable pageable);

        Page<HealthIssue> findByAnimalIdAndDiagnosisDateBetween(
                        Long animalId,
                        LocalDate startDate,
                        LocalDate endDate,
                        Pageable pageable);

        @Query("SELECT h FROM HealthIssue h WHERE h.animal.id = :animalId AND LOWER(h.name) LIKE LOWER(CONCAT('%', :name, '%')) AND h.diagnosisDate BETWEEN :startDate AND :endDate")
        Page<HealthIssue> findByAnimalIdAndNameContainingIgnoreCaseAndDiagnosisDateBetween(
                        @Param("animalId") Long animalId,
                        @Param("name") String name,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        Pageable pageable);

        @Query("SELECT h FROM HealthIssue h WHERE h.animal.id = :animalId AND h.animal.user.id = :userId")
        Page<HealthIssue> findByAnimalIdAndUserId(
                        @Param("animalId") Long animalId,
                        @Param("userId") Long userId,
                        Pageable pageable);

        Optional<HealthIssue> findByIdAndAnimalId(Long id, Long animalId);

        boolean existsByIdAndAnimalId(Long id, Long animalId);

        void deleteByIdAndAnimalId(Long id, Long animalId);

        @Query("SELECT h FROM HealthIssue h WHERE h.id = :healthIssueId AND h.animal.user.id = :userId")
        Optional<HealthIssue> findByIdAndUserId(@Param("healthIssueId") Long healthIssueId,
                        @Param("userId") Long userId);
}
