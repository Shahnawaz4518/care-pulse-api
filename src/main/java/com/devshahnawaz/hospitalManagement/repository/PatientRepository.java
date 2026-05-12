package com.devshahnawaz.hospitalManagement.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.devshahnawaz.hospitalManagement.dto.BloodGroupCountResponseEntity;
import com.devshahnawaz.hospitalManagement.entity.Patient;
import com.devshahnawaz.hospitalManagement.entity.type.BloodGroupType;

import jakarta.transaction.Transactional;

/**
 * PatientRepository - Spring Data JPA Repository for Patient entity.
 *
 * Demonstrates three query styles:
 * 1) JPA Derived Query Methods – method name parsed by Spring to build the
 * query automatically
 * 2) JPQL (@Query) – object-oriented query language using entity/field names
 * 3) Native SQL (nativeQuery) – raw SQL against the actual table/column names
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // ─────────────────────────────────────────────────────────────
    // ① JPA DERIVED QUERY METHODS
    // Spring Data JPA reads the method name and generates the
    // query automatically at startup – no SQL/JPQL needed.
    // ─────────────────────────────────────────────────────────────

    // JPA: SELECT p FROM Patient p WHERE p.name = ?1
    Patient findByName(String name);

    // JPA: SELECT p FROM Patient p WHERE p.birthDate = ?1 OR p.email = ?2
    List<Patient> findByBirthDateOrEmail(LocalDate birthDate, String email);

    // JPA: SELECT p FROM Patient p WHERE p.birthDate BETWEEN ?1 AND ?2
    List<Patient> findByBirthDateBetween(LocalDate startDate, LocalDate endDate);

    // JPA: SELECT p FROM Patient p WHERE p.name LIKE %?1% ORDER BY p.id DESC
    List<Patient> findByNameContainingOrderByIdDesc(String query);

    // ─────────────────────────────────────────────────────────────
    // ② JPQL (@Query) – Java Persistence Query Language
    // Uses entity class name (Patient) and Java field names,
    // NOT table/column names. Supports positional (?1) or named (:param).
    // ─────────────────────────────────────────────────────────────

    // JPQL: positional parameter (?1) – finds patients matching a blood group
    @Query("SELECT p FROM Patient p WHERE p.bloodGroup = ?1")
    List<Patient> findByBloodGroup(@Param("bloodGroup") BloodGroupType bloodGroup);

    // JPQL: named parameter (:birthDate) – finds patients born after a given date
    @Query("SELECT p FROM Patient p WHERE p.birthDate > :birthDate")
    List<Patient> findByBornAfterDate(@Param("birthDate") LocalDate birthDate);

    // JPQL: constructor expression – counts patients grouped by each blood group
    // Returns a typed projection instead of raw Object[].
    @Query("SELECT new com.devshahnawaz.hospitalManagement.dto.BloodGroupCountResponseEntity(p.bloodGroup, COUNT(p)) FROM Patient p GROUP BY p.bloodGroup")
    List<BloodGroupCountResponseEntity> countEachBloodGroupType();

    // ─────────────────────────────────────────────────────────────
    // ③ NATIVE SQL (@Query with nativeQuery = true)
    // Plain SQL against the actual DB table/column names.
    // Supports pagination via Pageable – returns a Page<Patient>.
    // ─────────────────────────────────────────────────────────────

    // NATIVE: raw SQL with pagination support
    @Query(value = "SELECT * FROM patient", nativeQuery = true)
    Page<Patient> findAllPatients(Pageable pageable);

    // ─────────────────────────────────────────────────────────────
    // ④ JPQL UPDATE with @Modifying
    // @Modifying marks the query as a DML statement (UPDATE/DELETE).
    // @Transactional is required so the change is committed to the DB.
    // ─────────────────────────────────────────────────────────────

    // JPQL Modifying: updates the patient name in-place; returns rows affected
    @Transactional
    @Modifying
    @Query("UPDATE Patient p SET p.name = :name WHERE p.id = :id")
    int updateNameWithId(@Param("name") String name, @Param("id") Long id);
}
