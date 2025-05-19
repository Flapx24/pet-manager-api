package com.example.demo.repositories;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Vaccine;

@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, Long> {
    
    // Find vaccines by animal ID with pagination
    Page<Vaccine> findByAnimalId(Long animalId, Pageable pageable);
    
    // Find vaccines by animal ID and application date range with pagination
    Page<Vaccine> findByAnimalIdAndApplicationDateBetween(
            Long animalId, 
            LocalDate startDate, 
            LocalDate endDate, 
            Pageable pageable);
    
    // Find vaccines by animal ID and expiration date range with pagination
    Page<Vaccine> findByAnimalIdAndExpirationDateBetween(
            Long animalId, 
            LocalDate startDate, 
            LocalDate endDate, 
            Pageable pageable);
    
    // Find vaccines by animal ID and validate user ownership
    @Query("SELECT v FROM Vaccine v WHERE v.animal.id = :animalId AND v.animal.user.id = :userId")
    Page<Vaccine> findByAnimalIdAndUserId(
            @Param("animalId") Long animalId, 
            @Param("userId") Long userId, 
            Pageable pageable);
            
    // Find vaccines by animal ID, filter by application date range and validate user ownership
    @Query("SELECT v FROM Vaccine v WHERE v.animal.id = :animalId AND v.animal.user.id = :userId " +
           "AND v.applicationDate BETWEEN :startDate AND :endDate")
    Page<Vaccine> findByAnimalIdAndUserIdAndApplicationDateBetween(
            @Param("animalId") Long animalId, 
            @Param("userId") Long userId, 
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
            
    // Find vaccines by animal ID, filter by expiration date range and validate user ownership
    @Query("SELECT v FROM Vaccine v WHERE v.animal.id = :animalId AND v.animal.user.id = :userId " +
           "AND v.expirationDate BETWEEN :startDate AND :endDate")
    Page<Vaccine> findByAnimalIdAndUserIdAndExpirationDateBetween(
            @Param("animalId") Long animalId, 
            @Param("userId") Long userId, 
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
            
    // Check if a vaccine exists for a given animal ID
    boolean existsByIdAndAnimalId(Long id, Long animalId);
    
    // Find a vaccine by ID and animal ID
    Optional<Vaccine> findByIdAndAnimalId(Long id, Long animalId);
    
    // Find vaccines with future expiration dates (non-expired vaccines)
    @Query("SELECT v FROM Vaccine v WHERE v.animal.id = :animalId AND v.animal.user.id = :userId " +
           "AND v.expirationDate IS NOT NULL AND v.expirationDate >= :currentDate")
    Page<Vaccine> findNonExpiredVaccinesByAnimalIdAndUserId(
            @Param("animalId") Long animalId, 
            @Param("userId") Long userId, 
            @Param("currentDate") LocalDate currentDate,
            Pageable pageable);
            
    // Find confirmed vaccines (vaccines with application date)
    @Query("SELECT v FROM Vaccine v WHERE v.animal.id = :animalId AND v.animal.user.id = :userId " +
           "AND v.applicationDate IS NOT NULL")
    Page<Vaccine> findConfirmedVaccinesByAnimalIdAndUserId(
            @Param("animalId") Long animalId, 
            @Param("userId") Long userId,
            Pageable pageable);
}
