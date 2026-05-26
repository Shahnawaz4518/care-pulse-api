package com.devshahnawaz.hospitalManagement;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.devshahnawaz.hospitalManagement.dto.BloodGroupCountResponseEntity;
import com.devshahnawaz.hospitalManagement.entity.Patient;
import com.devshahnawaz.hospitalManagement.entity.type.BloodGroupType;
import com.devshahnawaz.hospitalManagement.repository.PatientRepository;
import com.devshahnawaz.hospitalManagement.service.PatientService;

/**
 * PatientTest – Integration test class for verifying all query styles.
 *
 * @SpringBootTest loads the full application context so the real DB and
 *                 Spring beans (repository, service) are available during
 *                 tests.
 */
@SpringBootTest
public class PatientTest {

    // JPA: @Autowired injects the Spring Data JPA repository proxy at runtime
    @Autowired
    private PatientRepository patientRepository;

    // @Autowired injects the service bean (uses @Transactional internally)
    @Autowired
    private PatientService patientService;

    // ─────────────────────────────────────────────────────────────
    // TEST 1 – Basic JPA Repository Operations (findAll / save)
    // ─────────────────────────────────────────────────────────────
    @Test
    public void testPatientRepository() {

        // JPA: findAll() → SELECT * FROM patient (inherited from JpaRepository)
        List<Patient> patientList = patientRepository.findAll();
        System.out.println(patientList);

        // JPA: save() → INSERT INTO patient (...) VALUES (...)
        // We build a valid Patient to avoid NOT NULL constraint violations.
        // Patient p1 = new Patient();
        // p1.setName("Test Patient");
        // p1.setEmail("test.patient@gmail.com");
        // patientRepository.save(p1);
        // System.out.println("Saved: " + p1);
    }

    // ─────────────────────────────────────────────────────────────
    // TEST 2 – JPA Derived Queries, JPQL Queries, Native Query, DML
    // ─────────────────────────────────────────────────────────────
    @Test
    public void testTransactionMethods() {

        // ── JPA Derived Query Methods ─────────────────────────────
        // Spring generates the SQL from the method name automatically.

        // JPA: findByName → SELECT * FROM patient WHERE name = ?
        // Patient patient = patientRepository.findByName("Priya Verma");
        // System.out.println(patient);

        // JPA: findByBirthDateOrEmail → SELECT * FROM patient WHERE birth_date = ? OR
        // email = ?
        // List<Patient> byDateOrEmail = patientRepository.findByBirthDateOrEmail(
        // LocalDate.of(1993, 2, 10), "kavita.nair@gmail.com");

        // JPA: findByNameContainingOrderByIdDesc → SELECT * FROM patient WHERE name
        // LIKE '%h%' ORDER BY id DESC
        // List<Patient> byNameDesc =
        // patientRepository.findByNameContainingOrderByIdDesc("h");

        // JPA: findByBirthDateBetween → SELECT * FROM patient WHERE birth_date BETWEEN
        // ? AND ?
        // List<Patient> byDateRange = patientRepository.findByBirthDateBetween(
        // LocalDate.of(1990, 1, 1), LocalDate.of(1995, 12, 31));

        // ── JPQL Queries (@Query with entity/field names) ─────────
        // Uses Patient class name and Java field names (bloodGroup, birthDate).

        // JPQL: SELECT p FROM Patient p WHERE p.bloodGroup = ?1
        List<Patient> patientList = patientRepository.findByBloodGroup(BloodGroupType.A_POSITIVE);
        for (Patient patient : patientList) {
            System.out.println(patient);
        }

        // JPQL: SELECT p FROM Patient p WHERE p.birthDate > :birthDate
        // List<Patient> bornAfter =
        // patientRepository.findByBornAfterDate(LocalDate.of(1993, 1, 15));
        // for (Patient patient : bornAfter) {
        // System.out.println(patient);
        // }

        // JPQL: constructor projection – SELECT new
        // BloodGroupCountResponseEntity(p.bloodGroup, COUNT(p)) ...
        // List<BloodGroupCountResponseEntity> bloodGroupList =
        // patientRepository.countEachBloodGroupType();
        // for (BloodGroupCountResponseEntity item : bloodGroupList) {
        // System.out.println(item);
        // }

        // ── Native SQL Query (nativeQuery = true) ─────────────────
        // Raw SQL against actual table name "patient", with pagination.
        // NATIVE: SELECT * FROM patient (page 0, 2 records per page)
        Page<Patient> patients = patientRepository.findAllPatients(PageRequest.of(1, 2, Sort.by("name")));
        for (Patient patient : patients) {
            System.out.println(patient);
        }

        // ── JPQL Modifying Query (@Modifying + @Transactional) ────
        // UPDATE Patient p SET p.name = :name WHERE p.id = :id
        // Returns the number of rows updated.
        // int rowsUpdated = patientRepository.updateNameWithId("Aarav Sharma", 9L);
        // System.out.println("Rows updated: " + rowsUpdated);

        // ── Service-layer @Transactional demo ─────────────────────
        // PatientService.getPatientById() opens one transaction and calls
        // findById() twice – both calls share the first-level (session) cache.
        // Patient p = patientService.getPatientById(1L);
        // System.out.println(p);
    }
}
