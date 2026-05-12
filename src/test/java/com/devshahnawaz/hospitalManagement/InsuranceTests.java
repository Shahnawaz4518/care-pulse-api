package com.devshahnawaz.hospitalManagement;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.devshahnawaz.hospitalManagement.entity.Insurance;
import com.devshahnawaz.hospitalManagement.entity.Patient;
import com.devshahnawaz.hospitalManagement.service.InsuranceService;

@SpringBootTest
public class InsuranceTests {

    @Autowired
    private InsuranceService insuranceService;

    @Test
    public void testInsurance(){
        Insurance insurance = Insurance.builder()
        .policyNumber("HDFC_1234")
        .provider("HDFC")
        .validUntil(LocalDate.of(2038,12,12))
        .build();

        Patient patient = insuranceService.assignInsuranceToPatient(insurance, 1L);

        System.out.println(patient);
    }
}
