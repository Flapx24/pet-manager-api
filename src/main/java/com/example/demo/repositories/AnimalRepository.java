package com.example.demo.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Animal;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
        List<Animal> findByUserId(Long userId);

        Page<Animal> findByUserId(Long userId, Pageable pageable);

        @Query("SELECT a FROM Animal a WHERE a.user.id = :userId AND LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))")
        Page<Animal> findByUserIdAndNameContainingIgnoreCase(@Param("userId") Long userId, @Param("name") String name,
                        Pageable pageable);

        @Query("SELECT a FROM Animal a WHERE a.user.id = :userId AND a.class = :animalType")
        Page<Animal> findByUserIdAndAnimalType(@Param("userId") Long userId, @Param("animalType") String animalType,
                        Pageable pageable);

        Page<Animal> findByUserIdAndBirthDateBetween(Long userId, LocalDate startDate, LocalDate endDate,
                        Pageable pageable);

        @Query("SELECT a FROM Animal a WHERE a.user.id = :userId AND LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%')) AND a.class = :animalType")
        Page<Animal> findByUserIdAndNameContainingIgnoreCaseAndAnimalType(
                        @Param("userId") Long userId,
                        @Param("name") String name,
                        @Param("animalType") String animalType,
                        Pageable pageable);

        @Query("SELECT a FROM Animal a WHERE a.user.id = :userId AND LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%')) AND a.birthDate BETWEEN :startDate AND :endDate")
        Page<Animal> findByUserIdAndNameContainingIgnoreCaseAndBirthDateBetween(
                        @Param("userId") Long userId,
                        @Param("name") String name,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        Pageable pageable);

        @Query("SELECT a FROM Animal a WHERE a.user.id = :userId AND a.class = :animalType AND a.birthDate BETWEEN :startDate AND :endDate")
        Page<Animal> findByUserIdAndAnimalTypeAndBirthDateBetween(
                        @Param("userId") Long userId,
                        @Param("animalType") String animalType,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        Pageable pageable);

        @Query("SELECT a FROM Animal a WHERE a.user.id = :userId AND LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%')) AND a.class = :animalType AND a.birthDate BETWEEN :startDate AND :endDate")
        Page<Animal> findByUserIdAndNameContainingIgnoreCaseAndAnimalTypeAndBirthDateBetween(
                        @Param("userId") Long userId,
                        @Param("name") String name,
                        @Param("animalType") String animalType,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        Pageable pageable);

        @Query("SELECT DISTINCT a FROM Animal a JOIN a.vaccines v " +
                        "WHERE a.user.id = :userId " +
                        "AND v.applicationDate IS NULL " +
                        "AND v.expirationDate IS NOT NULL " +
                        "AND v.expirationDate >= :currentDate")
        Page<Animal> findAnimalsWithPendingVaccines(
                        @Param("userId") Long userId,
                        @Param("currentDate") LocalDate currentDate,
                        Pageable pageable);

        Optional<Animal> findByIdAndUserId(Long id, Long userId);

        boolean existsByIdAndUserId(Long id, Long userId);
}