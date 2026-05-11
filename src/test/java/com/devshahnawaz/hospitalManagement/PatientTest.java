package com.devshahnawaz.hospitalManagement;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devshahnawaz.hospitalManagement.entity.Patient;
import com.devshahnawaz.hospitalManagement.entity.type.BloodGroupType;
import com.devshahnawaz.hospitalManagement.repository.PatientRepository;
import com.devshahnawaz.hospitalManagement.service.PatientService;

// For Output testing Purpuse

@SpringBootTest
public class PatientTest {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientService patientService;

    @Test
    public void testPatientRepository() {

        List<Patient> patientList = patientRepository.findAll();
        System.out.println(patientList);

        Patient p1 = new Patient();
        patientRepository.save(p1);
    }

    @Test
    public void testTransactionMethods() {
        // Patient patient = patientService.getPatientById(1L);

        // Patient patient = patientRepository.findByName("Priya Verma");

        // List<Patient> patientList =
        // patientRepository.findByBirthDateOrEmail(LocalDate.of(1993,02,
        // 10),"kavita.nair@gmail.com");
        // List<Patient> patientList =
        // patientRepository.findByNameContainingOrderByIdDesc("h");

        // for(Patient patient: patientList){
        // System.out.println(patient);
        // }

        // List<Patient> patientList = patientRepository.findByBloodGroup(BloodGroupType.A_POSITIVE);

        // for (Patient patient : patientList) {
        //     System.out.println(patient);
        // }

        List<Patient> patientList = patientRepository.findByBornAfterDate(LocalDate.of(1993,1,15));

        for (Patient patient : patientList) {
            System.out.println(patient);
        }
    }
}
