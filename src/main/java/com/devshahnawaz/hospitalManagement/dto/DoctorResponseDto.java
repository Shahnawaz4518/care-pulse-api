package com.devshahnawaz.hospitalManagement.dto;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponseDto {

    private Long id;
    private String name;
    private String specialization;
    private String email;
    private Set<String> departmentNames;
    private List<Long> appointmentIds;
}
