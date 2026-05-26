package com.devshahnawaz.hospitalManagement.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppointmentRequestDto {

    private LocalDateTime appointmentTime;
    private String reason;
    private Long patientId;
    private Long doctorId;
}
