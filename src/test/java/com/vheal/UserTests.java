package com.vheal;

import com.vheal.dao.RoleRepository;
import com.vheal.dao.UserRepository;
import com.vheal.entity.*;
import com.vheal.service.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @Order(1)
    public void testCreateUser(){
        User user = new User("xyz@gmail.com","abc123");
    }

    @Test
    @Order(2)
    public void testCreateUserWithId(){
        User user = new User(100,"xyz@gmail.com","abc123");
    }

    @Test
    @Order(3)
    public void testCreateDrugWithSetterAndGetter(){
        User user = new User();
        user.setId(101);
        user.getId();
        user.setEmail("xyz@gmail.com");
        user.getEmail();
        user.setPassword("abc123");
        user.getPassword();
    }

    @Test
    @Order(4)
    public void testDoctorPatientRolesSetterAndGetter(){
        User user = new User();
        Role role = new Role();
        Doctor doctor = new Doctor();
        Patient patient = new Patient();
        user.getRole();
        user.addRole(role);
        user.setRoles(roleRepository.findAll());
        user.getRoles();
        user.setDoctor(doctor);
        user.getDoctor();
        user.setPatient(patient);
        user.getPatient();
        user.addRole(role);
        user.getRole();
        user.toString();
    }

    @Test
    @Order(5)
    public void testUserService(){
        UserService userImpl = new UserImpl(userRepository);
        List<User> users = userImpl.findAll();

        User user = userImpl.findById(1);
        userImpl.save(user);
        User invalidUser = userImpl.findById(100);
        userImpl.deleteById(1);
        User userWithEmail = userImpl.findByEmail("trafalgarlaw@gmail.com");
    }

    @Test
    @Order(6)
    public void testuserDetails(){
        UserService userImpl = new UserImpl(userRepository);
        User user = userImpl.findById(1);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        userDetails.getAuthorities();
        userDetails.getPassword();
        userDetails.getUsername();
        userDetails.isAccountNonExpired();
        userDetails.isAccountNonLocked();
        userDetails.isCredentialsNonExpired();
        userDetails.isEnabled();
    }
    @Test
    @Order(6)
    public void testuserDetailsService(){
        CustomUserDetailsService customUserDetailsService = new CustomUserDetailsService(userRepository);
        customUserDetailsService.loadUserByUsername("trafalgarlaw@gmail.com");
        try {
            customUserDetailsService.loadUserByUsername("xyz@yahoo.com");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
