package com.devshahnawaz.hospitalManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devshahnawaz.hospitalManagement.entity.Insurance;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
    
}
