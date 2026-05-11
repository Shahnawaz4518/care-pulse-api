package com.devshahnawaz.hospitalManagement.service;

import org.springframework.stereotype.Service;

import com.devshahnawaz.hospitalManagement.entity.Patient;
import com.devshahnawaz.hospitalManagement.repository.PatientRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * PatientService – Service layer demonstrating JPA First-Level Cache
 * & @Transactional.
 *
 * JPA CONCEPTS USED HERE:
 * 
 * @Transactional – wraps the method in a DB transaction.
 *                All reads/writes inside share the same EntityManager
 *                (session).
 *                First-Level Cache (Session Cache) – within one @Transactional
 *                method, calling
 *                findById() for the same ID twice returns the SAME Java object
 *                from memory (no second SQL fired) → p1 == p2 is true.
 *                Dirty Checking – any change made to a JPA-managed entity
 *                (e.g., p1.setName("yoyo"))
 *                is automatically persisted at the end of the transaction
 *                WITHOUT calling save(). Hibernate detects the change via
 *                "dirty checking".
 */
@Service
@RequiredArgsConstructor // Lombok: generates a constructor injecting all final fields
                         // (patientRepository)
public class PatientService {

    // JPA: final field → injected via constructor (preferred over @Autowired on
    // field)
    private final PatientRepository patientRepository;

    /**
     * getPatientById – demonstrates First-Level Cache and Dirty Checking.
     *
     * @Transactional: opens an EntityManager/Session for the duration of this
     *                 method.
     *                 findById() (JPA): SELECT * FROM patient WHERE id = ?
     *                 – first call hits the DB and caches the entity
     *                 – second call returns the cached instance → p1 == p2 is TRUE
     *                 p1.setName("yoyo") triggers Dirty Checking → Hibernate fires
     *                 an UPDATE at commit.
     */
    @Transactional
    public Patient getPatientById(Long id) {

        // JPA: hits DB → SELECT * FROM patient WHERE id = ?
        Patient p1 = patientRepository.findById(id).orElseThrow();

        // JPA: First-Level Cache hit → no DB call; returns same object as p1
        Patient p2 = patientRepository.findById(id).orElseThrow();

        // Prints 'true' – both references point to the same entity in the session cache
        System.out.println("p1 == p2 (same cached instance): " + (p1 == p2));

        // JPA Dirty Checking: changing a managed entity's field auto-triggers
        // UPDATE patient SET name='yoyo' WHERE id=? at transaction commit
        p1.setName("yoyo");

        return p1;
    }

}
