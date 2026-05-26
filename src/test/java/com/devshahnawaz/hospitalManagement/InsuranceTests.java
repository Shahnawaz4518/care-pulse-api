package com.devshahnawaz.hospitalManagement;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.devshahnawaz.hospitalManagement.entity.Appointment;
import com.devshahnawaz.hospitalManagement.entity.Insurance;
import com.devshahnawaz.hospitalManagement.entity.Patient;
import com.devshahnawaz.hospitalManagement.service.AppoinmentService;
import com.devshahnawaz.hospitalManagement.service.InsuranceService;

@SpringBootTest
public class InsuranceTests {

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private AppoinmentService appointmeService;

    @Test
    public void testInsurance() {
        Insurance insurance = Insurance.builder()
                .policyNumber("HDFC_1234")
                .provider("HDFC")
                .validUntil(LocalDate.of(2038, 12, 12))
                .build();

        Patient patient = insuranceService.assignInsuranceToPatient(insurance, 1L);

        System.out.println(patient);
    }

    @Test
    public void testCreateAppoinment() {
        Appointment appointment = Appointment.builder()
                .appointmentTime(LocalDateTime.of(2025, 11, 1, 14, 0, 0))
                .reason("Cancer")
                .build();

        var newAppointment = appointmeService.createNewAppointment(appointment, 1L, 2L);

        System.out.println(newAppointment);

        var updatedAppointment = appointmeService.reAssignAppointmentToAnotherDoctor(newAppointment.getId(), 3L);

        System.out.println(updatedAppointment);
    }
}
