package com.devshahnawaz.hospitalManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devshahnawaz.hospitalManagement.entity.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
}
