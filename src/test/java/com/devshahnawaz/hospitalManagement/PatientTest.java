package com.devshahnawaz.hospitalManagement;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.devshahnawaz.hospitalManagement.dto.BloodGroupCountResponseEntity;
import com.devshahnawaz.hospitalManagement.entity.Patient;
import com.devshahnawaz.hospitalManagement.entity.type.BloodGroupType;
import com.devshahnawaz.hospitalManagement.repository.PatientRepository;
import com.devshahnawaz.hospitalManagement.service.PatientService;

// FIX: Removed unused imports – @Query and @Param belong in the repository layer,
//      not in a test class. Keeping them here caused a compile warning.

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

        // FIX: The original code saved an empty Patient() which violated the
        // NOT NULL constraints on 'name' and 'email' → DataIntegrityViolationException.
        // Now we build a valid Patient before saving.
        // JPA: save() → INSERT INTO patient (...) VALUES (...)
        Patient p1 = new Patient();
        p1.setName("Test Patient");
        p1.setEmail("test.patient@gmail.com");
        patientRepository.save(p1);
        System.out.println("Saved: " + p1);
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

        // JPA: findByBirthDateOrEmail → SELECT * FROM patient WHERE birth_date=? OR
        // email=?
        // List<Patient> patientList =
        // patientRepository.findByBirthDateOrEmail(LocalDate.of(1993, 2, 10),
        // "kavita.nair@gmail.com");

        // JPA: findByNameContainingOrderByIdDesc → SELECT * FROM patient WHERE name
        // LIKE '%h%' ORDER BY id DESC
        // List<Patient> patientList =
        // patientRepository.findByNameContainingOrderByIdDesc("h");

        // JPA: findByBirthDateBetween → SELECT * FROM patient WHERE birth_date BETWEEN
        // ? AND ?
        // List<Patient> patientList = patientRepository.findByBirthDateBetween(
        // LocalDate.of(1990, 1, 1), LocalDate.of(1995, 12, 31));

        // ── JPQL Queries (@Query with entity/field names) ─────────
        // Uses Patient class name and Java field names (bloodGroup, birthDate).

        // JPQL: SELECT p FROM Patient p WHERE p.bloodGroup = ?1
        List<Patient> patientList =
        patientRepository.findByBloodGroup(BloodGroupType.A_POSITIVE);
        for (Patient patient : patientList) { System.out.println(patient); }

        // JPQL: SELECT p FROM Patient p WHERE p.birthDate > :birthDate
        // List<Patient> patientList = patientRepository.findByBornAfterDate(LocalDate.of(1993, 1, 15));
        // for (Patient patient : patientList) {
        //     System.out.println(patient);
        // }

        // JPQL: SELECT p.bloodGroup, COUNT(p) FROM Patient p GROUP BY p.bloodGroup
        // Returns Object[] because result is a projection (not a full entity)
        // List<Object[]> bloodGroupList = patientRepository.countEachBloodGroupType();
        // for (Object[] objects : bloodGroupList) {
        // System.out.println(objects[0] + " → count: " + objects[1]);
        // }

        // ── Native SQL Query (nativeQuery = true) ─────────────────
        // Raw SQL against actual table name "patient", not the entity name.
        // NATIVE: SELECT * FROM patient
        List<Patient> patients = patientRepository.findAllPatients();
        System.out.println("All patients (native): " + patients.size());

        // ── JPQL Modifying Query (@Modifying + @Transactional) ────
        // UPDATE Patient p SET p.name = :name WHERE p.id = :id
        // Returns the number of rows updated.
        int rowsUpdated = patientRepository.updateNameWithId("Aarav Sharma", 9L);
        System.out.println("Rows updated: " + rowsUpdated);

        // ── Service-layer @Transactional demo ─────────────────────
        // PatientService.getPatientById() opens one transaction and calls
        // findById() twice – both calls return the SAME cached entity from
        // the first-level (session) cache → p1 == p2 prints 'true'.
        // Patient p = patientService.getPatientById(1L);
        // System.out.println(p);

        // ── JPQL Projection Query (interface-based) ───────────────
        // JPQL: SELECT p.bloodGroup, COUNT(p) FROM Patient p GROUP BY p.bloodGroup
        // Returns BloodGroupCountResponseEntity projection (not a raw Object[]).
        List<BloodGroupCountResponseEntity> bloodGroupList = patientRepository.countEachBloodGroupType();
        for (BloodGroupCountResponseEntity bloodGroupCountResponse : bloodGroupList) {
            System.out.println(bloodGroupCountResponse);
        }
    }

}
