package com.devshahnawaz.hospitalManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devshahnawaz.hospitalManagement.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
}
