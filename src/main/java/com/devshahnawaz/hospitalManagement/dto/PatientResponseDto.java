package com.devshahnawaz.hospitalManagement.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.devshahnawaz.hospitalManagement.entity.type.BloodGroupType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponseDto {

    private Long id;
    private String name;
    private LocalDate birthDate;
    private String email;
    private String gender;
    private LocalDateTime createdAt;
    private BloodGroupType bloodGroup;
    private Long insuranceId;
    private List<Long> appointmentIds;
}
