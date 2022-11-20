package com.vheal;

import com.vheal.dao.*;
import com.vheal.entity.Doctor;
import com.vheal.entity.Drug;
import com.vheal.entity.Patient;
import com.vheal.entity.User;
import com.vheal.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
public class DoctorPatientControllerTests {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private DoctorService doctorService;

    @MockBean
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientService patientService;

    @MockBean
    private PatientRepository patientRepository;

    @Autowired
    private PrescriptionService prescriptionService;

    @MockBean
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private DrugService drugService;

    @MockBean
    private DrugRepository drugRepository;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private UserService userService;

    @Test
    @WithMockUser(username = "trafalgarlaw@gmail.com", authorities = {"DOCTOR"})
    public void testSearchPatients() throws Exception{
        Doctor doctor = new Doctor(1,"Viswa","9876543210","23","male","eye","MS, MD","Chennai","Tamil nadu","India");
        when(doctorRepository.findById(1)).thenReturn(Optional.of(doctor));
        Patient patient = new Patient(1,"yor","9876543210","23","male","Chennai","Tamil nadu","India");
        doctor.addPatient(patient);
        User user = new User(1,"trafalgarlaw@gmail.com","abc123");
        doctor.setUser(user);
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        when(patientService.findAll()).thenReturn(
                Stream.of(new Patient(1,"yor","9876543210","23","male","Chennai","Tamil nadu","India"),
                        new Patient(2,"thorn princess","9876543210","23","male","Chennai","Tamil nadu","India"))
                        .collect(Collectors.toList())
        );
        mockMvc.perform(get("/doctors/searchPatients")
                        .param("id","1").param("patientSearch","yor"))
                        .andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "trafalgarlaw@gmail.com", authorities = {"DOCTOR"})
    public void testSearchPatientsEmpty() throws Exception{
        Doctor doctor = new Doctor(1,"Viswa","9876543210","23","male","eye","MS, MD","Chennai","Tamil nadu","India");
        when(doctorRepository.findById(1)).thenReturn(Optional.of(doctor));
        Patient patient = new Patient(1,"yor","9876543210","23","male","Chennai","Tamil nadu","India");
        doctor.addPatient(patient);
        User user = new User(1,"trafalgarlaw@gmail.com","abc123");
        doctor.setUser(user);
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        when(patientService.findAll()).thenReturn(
                Stream.of(new Patient(1,"yor","9876543210","23","male","Chennai","Tamil nadu","India"),
                                new Patient(2,"thorn princess","9876543210","23","male","Chennai","Tamil nadu","India"))
                        .collect(Collectors.toList())
        );
        mockMvc.perform(get("/doctors/searchPatients")
                        .param("id","1").param("patientSearch",""))
                .andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "trafalgarlaw@gmail.com", authorities = {"DOCTOR"})
    public void testAddDoctorDuplicatePatient() throws Exception{
        Patient patient = new Patient(1,"yor","9876543210","23","male","Chennai","Tamil nadu","India");
        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        Doctor doctor = new Doctor(1,"Viswa","9876543210","23","male","eye","MS, MD","Chennai","Tamil nadu","India");
        when(doctorRepository.findById(1)).thenReturn(Optional.of(doctor));
        doctor.addPatient(patient);
        User user = new User(1,"trafalgarlaw@gmail.com","abc123");
        doctor.setUser(user);
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mockMvc.perform(get("/doctors/addDoctorPatient")
                        .param("patientId","1").param("doctorId","1"))
                .andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "trafalgarlaw@gmail.com", authorities = {"DOCTOR"})
    public void testAddDoctorPatient() throws Exception{
        Patient patient = new Patient(1,"yor","9876543210","23","male","Chennai","Tamil nadu","India");
        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        Doctor doctor = new Doctor(1,"Viswa","9876543210","23","male","eye","MS, MD","Chennai","Tamil nadu","India");
        when(doctorRepository.findById(1)).thenReturn(Optional.of(doctor));
        doctor.addPatient(patient);
        Patient patient2 = new Patient(2,"thorn princess","9876543210","23","male","Chennai","Tamil nadu","India");
        when(patientRepository.findById(2)).thenReturn(Optional.of(patient2));
        User user = new User(1,"trafalgarlaw@gmail.com","abc123");
        doctor.setUser(user);
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        when(patientRepository.save(patient2)).thenReturn(patient2);
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mockMvc.perform(get("/doctors/addDoctorPatient")
                        .param("patientId","2").param("doctorId","1"))
                .andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "trafalgarlaw@gmail.com", authorities = {"DOCTOR"})
    public void testDeletePatient() throws Exception{
        Patient patient = new Patient(1,"yor","9876543210","23","male","Chennai","Tamil nadu","India");
        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        Doctor doctor = new Doctor(1,"Viswa","9876543210","23","male","eye","MS, MD","Chennai","Tamil nadu","India");
        when(doctorRepository.findById(1)).thenReturn(Optional.of(doctor));
        doctor.addPatient(patient);
        User user = new User(1,"trafalgarlaw@gmail.com","abc123");
        doctor.setUser(user);
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        when(patientRepository.save(patient)).thenReturn(patient);
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mockMvc.perform(get("/doctors/deleteDoctorPatient")
                        .param("doctorPatientId","1").param("doctorId","1"))
                .andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "trafalgarlaw@gmail.com", authorities = {"DOCTOR"})
    public void testSearchDoctorPatients() throws Exception{
        Doctor doctor = new Doctor(1,"Viswa","9876543210","23","male","eye","MS, MD","Chennai","Tamil nadu","India");
        when(doctorRepository.findById(1)).thenReturn(Optional.of(doctor));
        Patient patient = new Patient(1,"yor","9876543210","23","male","Chennai","Tamil nadu","India");
        Patient patient2 = new Patient(2,"thorn princess","9876543210","23","male","Chennai","Tamil nadu","India");
        doctor.addPatient(patient);
        doctor.addPatient(patient2);
        User user = new User(1,"trafalgarlaw@gmail.com","abc123");
        doctor.setUser(user);
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mockMvc.perform(get("/doctors/searchDoctorPatients")
                        .param("id","1").param("doctorPatientSearch","yor"))
                .andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "yor@gmail.com", authorities = {"PATIENT"})
    public void testShowFormForUpdatePatients() throws Exception{
        Patient patient = new Patient(1,"yor","9876543210","23","male","Chennai","Tamil nadu","India");
        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        User user = new User(1,"yor@gmail.com","abc123");
        patient.setUser(user);
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mockMvc.perform(get("/doctors/patients/showFormForUpdatePatient")
                        .param("patientId","1"))
                        .andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "trafalgarlaw@gmail.com", authorities = {"DOCTOR"})
    public void testShowPatient() throws Exception{
        Patient patient = new Patient(1,"yor","9876543210","23","male","Chennai","Tamil nadu","India");
        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        Doctor doctor = new Doctor(1,"Viswa","9876543210","23","male","eye","MS, MD","Chennai","Tamil nadu","India");
        when(doctorRepository.findById(1)).thenReturn(Optional.of(doctor));
        doctor.addPatient(patient);
        User user = new User(1,"trafalgarlaw@gmail.com","abc123");
        patient.setUser(user);
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mockMvc.perform(get("/doctors/showPatient")
                        .param("doctorPatientId","1").param("doctorId","1"))
                        .andExpect(status().is(200));
    }
}
