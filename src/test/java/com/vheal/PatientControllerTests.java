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
public class PatientControllerTests {

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
    @WithMockUser(username = "yor@gmail.com", authorities = {"PATIENT"})
    public void testSaveInvalidPatient() throws Exception{
        Patient patient = new Patient(1,"yor","","23","male","Chennai","Tamil nadu","India");
        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        User user = new User(1,"yor@gmail.com","abc123");
        patient.setUser(user);
        when(patientRepository.save(patient)).thenReturn(patient);
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mockMvc.perform(post("/patients/savePatient")
                        .flashAttr("patient",patient))
                        .andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "yor@gmail.com", authorities = {"PATIENT"})
    public void testShowFormForUpdatePatient() throws Exception{
        Patient patient = new Patient(1,"yor","","23","male","Chennai","Tamil nadu","India");
        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        User user = new User(1,"yor@gmail.com","abc123");
        patient.setUser(user);
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mockMvc.perform(get("/patients/showFormForUpdatePatient")
                        .param("patientId","1"))
                        .andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "yor@gmail.com", authorities = {"PATIENT"})
    public void testShowFormForUpdatePatients() throws Exception{
        Patient patient = new Patient(1,"yor","","23","male","Chennai","Tamil nadu","India");
        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        User user = new User(1,"yor@gmail.com","abc123");
        patient.setUser(user);
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mockMvc.perform(get("/patients/patients/showFormForUpdatePatient")
                        .param("patientId","1"))
                .andExpect(status().is(200));
    }

}
