package com.devshahnawaz.hospitalManagement.service;

import org.springframework.stereotype.Service;

import com.devshahnawaz.hospitalManagement.entity.Appointment;
import com.devshahnawaz.hospitalManagement.entity.Doctor;
import com.devshahnawaz.hospitalManagement.entity.Patient;
import com.devshahnawaz.hospitalManagement.repository.AppointmentRepository;
import com.devshahnawaz.hospitalManagement.repository.DoctorRepository;
import com.devshahnawaz.hospitalManagement.repository.PatientRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppoinmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Transactional
    public Appointment createNewAppointment(Appointment appointment, Long doctorId, Long patientId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
        Patient patient = patientRepository.findById(patientId).orElseThrow();

        if (appointment.getId() != null)
            throw new IllegalArgumentException("Appointment Should not have Id");

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        patient.getAppointments().add(appointment); // to maintain bidirectional consistency

        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment reAssignAppointmentToAnotherDoctor(Long appointmentId, Long doctorId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow();
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();

        appointment.setDoctor(doctor); // thiswill automatically call the update, because it is dirty now

        doctor.getAppointments().add(appointment); // just for bidirectional consistency

        return appointment;
    }
}
