package com.devshahnawaz.hospitalManagement.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devshahnawaz.hospitalManagement.dto.BloodGroupCountResponseEntity;
import com.devshahnawaz.hospitalManagement.dto.CreatePatientRequestDto;
import com.devshahnawaz.hospitalManagement.dto.PatientResponseDto;
import com.devshahnawaz.hospitalManagement.dto.UpdatePatientRequestDto;
import com.devshahnawaz.hospitalManagement.entity.Patient;
import com.devshahnawaz.hospitalManagement.entity.type.BloodGroupType;
import com.devshahnawaz.hospitalManagement.service.PatientService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {
        List<PatientResponseDto> patients = patientService.getAllPatients()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<PatientResponseDto>> getAllPatientsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PatientResponseDto> patients = patientService.getAllPatientsPaginated(pageable)
                .map(this::toResponseDto);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDto> getPatientById(@PathVariable Long id) {
        Patient patient = patientService.getPatient(id);
        return ResponseEntity.ok(toResponseDto(patient));
    }

    @PostMapping
    public ResponseEntity<PatientResponseDto> createPatient(@RequestBody CreatePatientRequestDto dto) {
        Patient patient = new Patient();
        patient.setName(dto.getName());
        patient.setBirthDate(dto.getBirthDate());
        patient.setEmail(dto.getEmail());
        patient.setGender(dto.getGender());
        patient.setBloodGroup(dto.getBloodGroup());

        Patient saved = patientService.createPatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDto> updatePatient(
            @PathVariable Long id,
            @RequestBody UpdatePatientRequestDto dto) {
        Patient updated = patientService.updatePatient(id, dto);
        return ResponseEntity.ok(toResponseDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<PatientResponseDto>> searchPatients(@RequestParam String query) {
        List<PatientResponseDto> patients = patientService.searchPatientsByName(query)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/blood-group/{bloodGroup}")
    public ResponseEntity<List<PatientResponseDto>> getByBloodGroup(@PathVariable BloodGroupType bloodGroup) {
        List<PatientResponseDto> patients = patientService.getPatientsByBloodGroup(bloodGroup)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/blood-group-counts")
    public ResponseEntity<List<BloodGroupCountResponseEntity>> getBloodGroupCounts() {
        return ResponseEntity.ok(patientService.getBloodGroupCounts());
    }

    @GetMapping("/with-appointments")
    public ResponseEntity<List<PatientResponseDto>> getPatientsWithAppointments() {
        List<PatientResponseDto> patients = patientService.getAllPatientsWithAppointments()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(patients);
    }

    // ─── Helper: Entity → Response DTO ──────────────────────────────────

    private PatientResponseDto toResponseDto(Patient patient) {
        return PatientResponseDto.builder()
                .id(patient.getId())
                .name(patient.getName())
                .birthDate(patient.getBirthDate())
                .email(patient.getEmail())
                .gender(patient.getGender())
                .createdAt(patient.getCreatedAt())
                .bloodGroup(patient.getBloodGroup())
                .insuranceId(patient.getInsurance() != null ? patient.getInsurance().getId() : null)
                .appointmentIds(
                        patient.getAppointments() != null
                                ? patient.getAppointments().stream()
                                        .map(a -> a.getId())
                                        .collect(Collectors.toList())
                                : List.of())
                .build();
    }
}
