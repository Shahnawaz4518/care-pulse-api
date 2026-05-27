package com.devshahnawaz.hospitalManagement.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devshahnawaz.hospitalManagement.dto.AppointmentResponseDto;
import com.devshahnawaz.hospitalManagement.dto.DoctorResponseDto;
import com.devshahnawaz.hospitalManagement.dto.PatientResponseDto;
import com.devshahnawaz.hospitalManagement.entity.Appointment;
import com.devshahnawaz.hospitalManagement.entity.Doctor;
import com.devshahnawaz.hospitalManagement.entity.Patient;
import com.devshahnawaz.hospitalManagement.repository.AppointmentRepository;
import com.devshahnawaz.hospitalManagement.repository.DepartmentRepository;
import com.devshahnawaz.hospitalManagement.repository.DoctorRepository;
import com.devshahnawaz.hospitalManagement.repository.InsuranceRepository;
import com.devshahnawaz.hospitalManagement.repository.PatientRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final DepartmentRepository departmentRepository;
    private final InsuranceRepository insuranceRepository;

    // ─── Dashboard Stats ────────────────────────────────────────────────

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Long>> getDashboardStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalPatients", patientRepository.count());
        stats.put("totalDoctors", doctorRepository.count());
        stats.put("totalAppointments", appointmentRepository.count());
        stats.put("totalDepartments", departmentRepository.count());
        stats.put("totalInsurances", insuranceRepository.count());
        return ResponseEntity.ok(stats);
    }

    // ─── Patient Management ─────────────────────────────────────────────

    @GetMapping("/patients")
    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {
        List<PatientResponseDto> patients = patientRepository.findAll()
                .stream()
                .map(this::toPatientResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/patients/{id}")
    public ResponseEntity<PatientResponseDto> getPatientById(@PathVariable Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + id));
        return ResponseEntity.ok(toPatientResponseDto(patient));
    }

    @DeleteMapping("/patients/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        if (!patientRepository.existsById(id)) {
            throw new EntityNotFoundException("Patient not found with ID: " + id);
        }
        patientRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ─── Doctor Management ──────────────────────────────────────────────

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorResponseDto>> getAllDoctors() {
        List<DoctorResponseDto> doctors = doctorRepository.findAll()
                .stream()
                .map(this::toDoctorResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/doctors/{id}")
    public ResponseEntity<DoctorResponseDto> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + id));
        return ResponseEntity.ok(toDoctorResponseDto(doctor));
    }

    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new EntityNotFoundException("Doctor not found with ID: " + id);
        }
        doctorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ─── Appointment Management ─────────────────────────────────────────

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointments() {
        List<AppointmentResponseDto> appointments = appointmentRepository.findAll()
                .stream()
                .map(this::toAppointmentResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/appointments/{id}")
    public ResponseEntity<AppointmentResponseDto> getAppointmentById(@PathVariable Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with ID: " + id));
        return ResponseEntity.ok(toAppointmentResponseDto(appointment));
    }

    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Appointment not found with ID: " + id);
        }
        appointmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ─── Helper: Entity → Response DTO ──────────────────────────────────

    private PatientResponseDto toPatientResponseDto(Patient patient) {
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
                                        .map(Appointment::getId)
                                        .collect(Collectors.toList())
                                : List.of())
                .build();
    }

    private DoctorResponseDto toDoctorResponseDto(Doctor doctor) {
        return DoctorResponseDto.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .specialization(doctor.getSpecialization())
                .email(doctor.getEmail())
                .departmentNames(
                        doctor.getDepartments() != null
                                ? doctor.getDepartments().stream()
                                        .map(d -> d.getName())
                                        .collect(Collectors.toSet())
                                : null)
                .appointmentIds(
                        doctor.getAppointments() != null
                                ? doctor.getAppointments().stream()
                                        .map(Appointment::getId)
                                        .collect(Collectors.toList())
                                : List.of())
                .build();
    }

    private AppointmentResponseDto toAppointmentResponseDto(Appointment appointment) {
        return AppointmentResponseDto.builder()
                .id(appointment.getId())
                .appointmentTime(appointment.getAppointmentTime())
                .reason(appointment.getReason())
                .patientId(appointment.getPatient() != null ? appointment.getPatient().getId() : null)
                .patientName(appointment.getPatient() != null ? appointment.getPatient().getName() : null)
                .doctorId(appointment.getDoctor() != null ? appointment.getDoctor().getId() : null)
                .doctorName(appointment.getDoctor() != null ? appointment.getDoctor().getName() : null)
                .build();
    }
}
