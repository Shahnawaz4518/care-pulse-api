package com.devshahnawaz.hospitalManagement.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devshahnawaz.hospitalManagement.dto.BloodGroupCountResponseEntity;
import com.devshahnawaz.hospitalManagement.dto.UpdatePatientRequestDto;
import com.devshahnawaz.hospitalManagement.entity.Patient;
import com.devshahnawaz.hospitalManagement.entity.type.BloodGroupType;
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

    @Transactional
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Transactional
    public Page<Patient> getAllPatientsPaginated(Pageable pageable) {
        return patientRepository.findAllPatients(pageable);
    }

    @Transactional
    public Patient getPatient(Long id) {
        return patientRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Patient not found with id: " + id));
    }

    @Transactional
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Transactional
    public Patient updatePatient(Long id, UpdatePatientRequestDto dto) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Patient not found with id: " + id));

        if (dto.getName() != null) {
            patient.setName(dto.getName());
        }
        if (dto.getBirthDate() != null) {
            patient.setBirthDate(dto.getBirthDate());
        }
        if (dto.getEmail() != null) {
            patient.setEmail(dto.getEmail());
        }
        if (dto.getGender() != null) {
            patient.setGender(dto.getGender());
        }
        if (dto.getBloodGroup() != null) {
            patient.setBloodGroup(dto.getBloodGroup());
        }

        return patientRepository.save(patient);
    }

    @Transactional
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    @Transactional
    public List<Patient> searchPatientsByName(String query) {
        return patientRepository.findByNameContainingOrderByIdDesc(query);
    }

    @Transactional
    public List<Patient> getPatientsByBloodGroup(BloodGroupType bloodGroup) {
        return patientRepository.findByBloodGroup(bloodGroup);
    }

    @Transactional
    public List<BloodGroupCountResponseEntity> getBloodGroupCounts() {
        return patientRepository.countEachBloodGroupType();
    }

    @Transactional
    public List<Patient> getAllPatientsWithAppointments() {
        return patientRepository.findAllPatientWithAppointment();
    }

}
