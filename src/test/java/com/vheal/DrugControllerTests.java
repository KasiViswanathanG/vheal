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
public class DrugControllerTests {

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
    @WithMockUser(username = "trafalgarlaw@gmail.com", authorities = {"DOCTOR"})
    public void testListDrug() throws Exception{
        when(drugRepository.findAll()).thenReturn(
                Stream.of(new Drug(1,"no cold","syrup"),
                        new Drug(2,"covishield","syringe")).collect(Collectors.toList())
        );
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mockMvc.perform(get("/drugs/list")).andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "trafalgarlaw@gmail.com", authorities = {"DOCTOR"})
    public void testShowFormForAdd() throws Exception{
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mockMvc.perform(get("/drugs/showFormForAdd")).andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "trafalgarlaw@gmail.com", authorities = {"DOCTOR"})
    public void testShowFormForUpdate() throws Exception{
        Drug drug = new Drug(1,"no cold","syrup");
        when(drugRepository.findById(1)).thenReturn(Optional.of(drug));

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mockMvc.perform(get("/drugs/showFormForUpdate")
                        .param("drugId","1"))
                        .andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "trafalgarlaw@gmail.com", authorities = {"DOCTOR"})
    void testSaveDrugEmpty() throws Exception {
        Drug drug = new Drug(1,"metacin","");

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mockMvc.perform(post("/drugs/save")
                        .flashAttr("drug",drug))
                        .andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "trafalgarlaw@gmail.com", authorities = {"DOCTOR"})
    void testSaveDrug() throws Exception {
        Drug drug = new Drug(1,"metacin","tablet");
        when(drugRepository.save(drug)).thenReturn(drug);
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mockMvc.perform(post("/drugs/save")
                        .flashAttr("drug",drug))
                        .andExpect(status().is(302));
    }

    @Test
    @WithMockUser(username = "trafalgarlaw@gmail.com", authorities = {"DOCTOR"})
    void testDelete() throws Exception {

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mockMvc.perform(get("/drugs/delete").param("drugId","1"))
                .andExpect(status().is(302));
    }

    @Test
    @WithMockUser(username = "trafalgarlaw@gmail.com", authorities = {"DOCTOR"})
    void testSearchDrug() throws Exception {
        Drug drug = new Drug(1,"metacin","tablet");
        when(drugService.findAll()).thenReturn(
                Stream.of(new Drug(1,"metacin","tablet"),
                        new Drug(2,"paracetemol","tablet"),
                        new Drug(3,"covishield","syringe")).collect(Collectors.toList())
        );
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mockMvc.perform(get("/drugs/search")
                        .param("drugSearch","metacin"))
                        .andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "trafalgarlaw@gmail.com", authorities = {"DOCTOR"})
    void testSearchType() throws Exception {
        Drug drug = new Drug(1,"metacin","tablet");
        when(drugService.findAll()).thenReturn(
                Stream.of(new Drug(1,"metacin","tablet"),
                        new Drug(2,"paracetemol","tablet"),
                        new Drug(3,"covishield","syringe")).collect(Collectors.toList())
        );
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mockMvc.perform(get("/drugs/search")
                        .param("drugSearch","syringe"))
                .andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "trafalgarlaw@gmail.com", authorities = {"DOCTOR"})
    void testSearchEmpty() throws Exception {
        Drug drug = new Drug(1,"metacin","");
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mockMvc.perform(get("/drugs/search")
                        .param("drugSearch",""))
                .andExpect(status().is(302));
    }

}
