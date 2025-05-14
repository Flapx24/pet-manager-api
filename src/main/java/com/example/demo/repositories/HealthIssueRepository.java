package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.HealthIssue;

import java.util.List;
import java.util.Optional;

@Repository
public interface HealthIssueRepository extends JpaRepository<HealthIssue, Long> {

    List<HealthIssue> findByAnimalId(Long animalId);

    Optional<HealthIssue> findByIdAndAnimalId(Long id, Long animalId);

    boolean existsByIdAndAnimalId(Long id, Long animalId);

    void deleteByIdAndAnimalId(Long id, Long animalId);

    @Query("SELECT h FROM HealthIssue h WHERE h.id = :healthIssueId AND h.animal.user.id = :userId")
    Optional<HealthIssue> findByIdAndUserId(@Param("healthIssueId") Long healthIssueId, @Param("userId") Long userId);
}
