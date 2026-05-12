package com.devshahnawaz.hospitalManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devshahnawaz.hospitalManagement.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    
}
