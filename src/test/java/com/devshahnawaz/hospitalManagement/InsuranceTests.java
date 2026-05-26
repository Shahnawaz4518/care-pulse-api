package com.devshahnawaz.hospitalManagement;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.devshahnawaz.hospitalManagement.entity.Insurance;
import com.devshahnawaz.hospitalManagement.entity.Patient;
import com.devshahnawaz.hospitalManagement.service.AppointmentService;
import com.devshahnawaz.hospitalManagement.service.InsuranceService;

@SpringBootTest
public class InsuranceTests {

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private AppointmentService appointmentService;

    @Test
    public void testInsurance() {
        Insurance insurance = Insurance.builder()
                .policyNumber("HDFC_1234")
                .provider("HDFC")
                .validUntil(LocalDate.of(2038, 12, 12))
                .build();

        Patient patient = insuranceService.assignInsuranceToPatient(insurance, 1L);

        System.out.println(patient);

        var newPatient = insuranceService.disassociateInsuranceFromPatient(patient.getId());

        System.out.println(newPatient);
    }

    @Test
    public void testCreateAppoinment() {
        var createAppointmentDto = new com.devshahnawaz.hospitalManagement.dto.CreateAppointmentRequestDto();
        createAppointmentDto.setAppointmentTime(LocalDateTime.of(2025, 11, 1, 14, 0, 0));
        createAppointmentDto.setReason("Cancer");
        createAppointmentDto.setPatientId(1L);
        createAppointmentDto.setDoctorId(2L);

        var newAppointment = appointmentService.createNewAppointment(createAppointmentDto);

        System.out.println(newAppointment);

        var updatedAppointment = appointmentService.reAssignAppointmentToAnotherDoctor(newAppointment.getId(), 3L);

        System.out.println(updatedAppointment);
    }
}
