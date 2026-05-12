package com.devshahnawaz.hospitalManagement.service;

import org.springframework.stereotype.Service;

import com.devshahnawaz.hospitalManagement.repository.InsuranceRepository;
import com.devshahnawaz.hospitalManagement.repository.PatientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InsuranceService {

    private InsuranceRepository insuranceRepository;
    private PatientRepository patientRepository;
}
