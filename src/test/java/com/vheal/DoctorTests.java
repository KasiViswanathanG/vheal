package com.vheal;

import com.vheal.dao.DoctorRepository;
import com.vheal.dao.DrugRepository;
import com.vheal.dao.PatientRepository;
import com.vheal.entity.*;
import com.vheal.service.DoctorImpl;
import com.vheal.service.DoctorService;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DoctorTests {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Test
    @Rollback(value = false)
    @Order(1)
    public void testCreateDoctor(){
        Doctor doctor = new Doctor("Viswa","9876543210","23","male","eye","MS, MD","Chennai","Tamil nadu","India");
        Doctor savedDoctor = doctorRepository.save(doctor);
        assertNotNull(savedDoctor);
    }

    @Test
    @Order(2)
    public void testFindDoctorByNameExist(){
        String doctorName = "Viswa";
        Doctor doctor = doctorRepository.findByDoctorName(doctorName);
        assertThat(doctor.getDoctorName()).isEqualTo(doctorName);
    }

    @Test
    @Order(3)
    public void testFindDoctorByNameNotExist(){
        String doctorName = "Nathan";
        Doctor doctor = doctorRepository.findByDoctorName(doctorName);
        assertNull(doctor);
    }

    @Test
    @Order(4)
    public void testUpdateDoctor(){
        String doctorName = "Kasi";
        Doctor doctor = new Doctor(doctorName,"9876543210","23","male","eye","MS, MD","Chennai","Tamil nadu","India");
        doctor.setAge("24");
        doctorRepository.save(doctor);
        Doctor updatedDoctor = doctorRepository.findByDoctorName(doctorName);
        assertThat(updatedDoctor.getDoctorName()).isEqualTo(doctorName);
    }

    @Test
    @Rollback(value = false)
    @Order(5)
    public void testListDoctor(){
        List<Doctor> doctors = doctorRepository.findAll();

        for (Doctor doctor : doctors){
            System.out.println(doctor);
        }
        assertThat(doctors).size().isGreaterThan(0);
    }

    @Test
    @Rollback(value = false)
    @Order(6)
    public void testDeleteDoctor(){
        String doctorName = "Viswa";
        Doctor doctor = doctorRepository.findByDoctorName(doctorName);
        Integer id = doctor.getId();
        boolean isExistBeforeDelete = doctorRepository.findById(id).isPresent();
        doctorRepository.deleteById(id);
        boolean notExistAfterDelete = doctorRepository.findById(id).isPresent();
        assertTrue(isExistBeforeDelete);
        assertFalse(notExistAfterDelete);
    }

    @Test
    @Order(7)
    public void testCreateDoctorWithId(){
        Doctor doctor = new Doctor(100,"Viswa","9876543210","23","male","Surgeon","MS","Chennai","Tamil nadu","India");
        Doctor savedDoctor = doctorRepository.save(doctor);
        assertNotNull(savedDoctor);
    }

    @Test
    @Order(8)
    public void testCreateDoctorWithGetterSetter(){
        Doctor doctor = new Doctor();
        doctor.setId(101);
        doctor.getId();
        doctor.setDoctorName("Viswa");
        doctor.getDoctorName();
        doctor.setPhoneNo("9876543210");
        doctor.getPhoneNo();
        doctor.setAge("23");
        doctor.getAge();
        doctor.setGender("male");
        doctor.getGender();
        doctor.setSpecialization("Surgeon");
        doctor.getSpecialization();
        doctor.setDegree("MS");
        doctor.getDegree();
        doctor.setCity("Chennai");
        doctor.getCity();
        doctor.setState("Tamil nadu");
        doctor.getState();
        doctor.setCountry("India");
        doctor.getCountry();
        Doctor savedDoctor = doctorRepository.save(doctor);
        assertNotNull(savedDoctor);
    }

    @Test
    @Order(9)
    public void testDoctorsUserAndPatient(){
        Doctor doctor = new Doctor();
        Patient patient = new Patient();
        User user = new User();
        doctor.setUser(user);
        doctor.getUser();
        doctor.addPatient(patient);
        doctor.deletePatient(patient);
        doctor.setPatients(patientRepository.findAll());
        doctor.getPatients();
        Optional<Patient> result = patientRepository.findById(1);
        Patient existpatient = result.get();
        doctor.addPatient(patient);
        doctor.deletePatient(patient);
    }

    @Test
    @Order(10)
    public void testDoctorService() {
        DoctorService doctorService = new DoctorImpl(doctorRepository);
        doctorService.findAll();
        doctorService.findById(1);
        doctorService.findById(100);
        Doctor doctor = new Doctor("Viswa","9876543210","23","male","Surgeon","MS","Chennai","Tamil nadu","India");
        doctorService.save(doctor);
        doctorService.deleteById(1);
    }

}
