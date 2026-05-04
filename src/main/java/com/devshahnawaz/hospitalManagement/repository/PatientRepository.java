package com.devshahnawaz.hospitalManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devshahnawaz.hospitalManagement.entity.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

}
