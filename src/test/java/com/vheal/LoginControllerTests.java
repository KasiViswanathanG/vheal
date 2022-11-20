package com.vheal;

import com.vheal.controller.LoginController;
import com.vheal.dao.DrugRepository;
import com.vheal.dao.RoleRepository;
import com.vheal.dao.UserRepository;
import com.vheal.entity.Drug;
import com.vheal.service.DoctorService;
import com.vheal.service.DrugService;
import com.vheal.service.PatientService;
import com.vheal.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
public class LoginControllerTests {

    @Autowired
    private DrugService drugService;
    @MockBean
    private DrugRepository drugRepository;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Test
    void testAccessDenied()
    {
        LoginController loginController = new LoginController(userRepo,roleRepo,userService,patientService,doctorService);
        String actual = loginController.showAccessDenied();
        String expected = "access-denied";
        Assertions.assertEquals(expected,actual);
    }

    @Test
    @WithMockUser(username = "trafalgarlaw@gmail.com", authorities = {"DOCTOR"})
    public void testViewHomePage() throws Exception{
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mockMvc.perform(get("/")).andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "trafalgarlaw@gmail.com", authorities = {"DOCTOR"})
    public void testShowRegistrationForm() throws Exception{
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mockMvc.perform(get("/register")).andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "trafalgarlaw@gmail.com", authorities = {"DOCTOR"})
    public void testLogin() throws Exception{
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mockMvc.perform(get("/login")).andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "trafalgarlaw@gmail.com", authorities = {"DOCTOR"})
    public void testListUsers() throws Exception{
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mockMvc.perform(get("/users")).andExpect(status().is(200));
    }

}
