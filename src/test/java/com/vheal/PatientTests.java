package com.vheal;

import com.vheal.dao.DoctorRepository;
import com.vheal.dao.DrugRepository;
import com.vheal.dao.PatientRepository;
import com.vheal.dao.PrescriptionRepository;
import com.vheal.entity.*;
import com.vheal.service.CustomUserDetailsService;
import com.vheal.service.PatientImpl;
import com.vheal.service.PatientService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PatientTests {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Test
    @Rollback(value = false)
    @Order(1)
    public void testCreatePatient(){
        Patient patient = new Patient("Viswa","9876543210","23","male","Chennai","Tamil nadu","India");
        Patient savedPatient = patientRepository.save(patient);
        assertNotNull(savedPatient);
    }

    @Test
    @Order(2)
    public void testFindPatientByNameExist(){
        String patientName = "Viswa";
        Patient patient = patientRepository.findByPatientName(patientName);
        assertThat(patient.getPatientName()).isEqualTo(patientName);
    }

    @Test
    @Order(3)
    public void testFindPatientByNameNotExist(){
        String patientName = "Nathan";
        Patient patient = patientRepository.findByPatientName(patientName);
        assertNull(patient);
    }

    @Test
    @Order(4)
    public void testUpdatePatient(){
        String patientName = "Kasi";
        Patient patient = new Patient(patientName,"9876543210","23","male","Chennai","Tamil nadu","India");
        patient.setAge("24");
        patientRepository.save(patient);
        Patient updatedPatient = patientRepository.findByPatientName(patientName);
        assertThat(updatedPatient.getPatientName()).isEqualTo(patientName);
    }

    @Test
    @Rollback(value = false)
    @Order(5)
    public void testListPatient(){
        List<Patient> patients = patientRepository.findAll();

        for (Patient patient : patients){
            System.out.println(patient);
        }
        assertThat(patients).size().isGreaterThan(0);
    }

    @Test
    @Rollback(value = false)
    @Order(6)
    public void testDeletePatient(){
        String patientName = "Viswa";
        Patient patient = patientRepository.findByPatientName(patientName);
        Integer id = patient.getId();
        boolean isExistBeforeDelete = patientRepository.findById(id).isPresent();
        patientRepository.deleteById(id);
        boolean notExistAfterDelete = patientRepository.findById(id).isPresent();
        assertTrue(isExistBeforeDelete);
        assertFalse(notExistAfterDelete);
    }

    @Test
    @Order(7)
    public void testCreatePatientWithId(){
        Patient patient = new Patient(100,"Viswa","9876543210","23","male","Chennai","Tamil nadu","India");
        Patient savedPatient = patientRepository.save(patient);
        assertNotNull(savedPatient);
    }

    @Test
    @Order(8)
    public void testCreatePatientWithGetterSetter(){
        Patient patient = new Patient();
        patient.setId(101);
        patient.getId();
        patient.setPatientName("Viswa");
        patient.getPatientName();
        patient.setPhoneNo("9876543210");
        patient.getPhoneNo();
        patient.setAge("23");
        patient.getAge();
        patient.setGender("male");
        patient.getGender();
        patient.setCity("Chennai");
        patient.getCity();
        patient.setState("Tamil nadu");
        patient.getState();
        patient.setCountry("India");
        patient.getCountry();
        Patient savedPatient = patientRepository.save(patient);
        assertNotNull(savedPatient);
    }

    @Test
    @Order(9)
    public void testPatientsUserDoctorAndPrescriptions(){
        Patient patient = new Patient();
        User user = new User();
        patient.setUser(user);
        patient.getUser();
        try {
            patient.getDoctor();
        }catch(Exception e){
            e.printStackTrace();
        }
        patient.setDoctors(doctorRepository.findAll());
        patient.getDoctors();
        try {
            patient.getDoctor();
        }catch(Exception e){
            e.printStackTrace();
        }
        patient.getPrescriptions();
        Prescription prescription = null;
        try {
            prescription = prescriptionRepository.findByDate("02-05-2022");
        }catch(Exception e){
            e.printStackTrace();
        }
        patient.addPrescription(prescription);
        patient.setPrescriptions(prescriptionRepository.findAll());
        patient.getPrescriptions();
        patient.addPrescription(prescription);
        try {
            patient.deletePrescription(prescription);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(10)
    public void testPatientService() {
        PatientService patientService = new PatientImpl(patientRepository);
        patientService.findAll();
        patientService.findById(1);
        patientService.findById(100);
        Patient patient = new Patient("Viswa","9876543210","23","male","Chennai","Tamil nadu","India");
        patientService.save(patient);
        patientService.deleteById(1);
    }
}
