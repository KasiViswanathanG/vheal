package com.vheal;

import com.vheal.dao.DoctorRepository;
import com.vheal.dao.RoleRepository;
import com.vheal.dao.UserRepository;
import com.vheal.entity.Doctor;
import com.vheal.entity.Patient;
import com.vheal.entity.Role;
import com.vheal.entity.User;
import com.vheal.service.UserImpl;
import com.vheal.service.UserService;
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
public class RoleTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @Order(1)
    public void testCreateRole(){
        Role role = new Role("technician");
    }

    @Test
    @Order(2)
    public void testCreateRoleWithId(){
        Role role = new Role(100,"technician");
    }

    @Test
    @Order(3)
    public void testCreateRoleWithSetterAndGetter(){
        Role role = new Role();
        role.setId(101);
        role.getId();
        role.setName("technician");
        role.getName();
        role.setUsers(userRepository.findAll());
        role.getUsers();
        role.toString();
    }
}
