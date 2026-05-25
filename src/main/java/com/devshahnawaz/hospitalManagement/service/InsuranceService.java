package com.devshahnawaz.hospitalManagement.service;

import org.springframework.stereotype.Service;

import com.devshahnawaz.hospitalManagement.entity.Insurance;
import com.devshahnawaz.hospitalManagement.entity.Patient;
import com.devshahnawaz.hospitalManagement.repository.InsuranceRepository;
import com.devshahnawaz.hospitalManagement.repository.PatientRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InsuranceService {

    private final InsuranceRepository insuranceRepository;
    private final PatientRepository patientRepository;

    @Transactional
    public Patient assignInsuranceToPatient(Insurance insurance, Long patientId){
        Patient patient = patientRepository.findById(patientId)
        .orElseThrow(() -> new EntityNotFoundException("Patient Not Found With id: "+ patientId));

        // FIX: Persist the transient Insurance entity first.
        // Without this, assigning a transient object to a managed entity causes
        // a TransientPropertyValueException at flush time.
        // Once saved, 'savedInsurance' is a managed entity within this transaction.
        Insurance savedInsurance = insuranceRepository.save(insurance);

        // Assign the now-managed Insurance to the managed Patient.
        // Hibernate dirty-checking will automatically fire:
        //   UPDATE patient SET patient_insurance_id = ? WHERE id = ?
        // at the end of the transaction — no explicit patientRepository.save() needed.
        patient.setInsurance(savedInsurance);

        // Maintain bi-directional in-memory consistency (inverse side — no DB effect).
        savedInsurance.setPatient(patient);

        return patient;
    }
}
