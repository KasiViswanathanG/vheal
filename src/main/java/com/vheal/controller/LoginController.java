package com.vheal.controller;

import com.vheal.dao.RoleRepository;
import com.vheal.dao.UserRepository;

import com.vheal.entity.*;
import com.vheal.service.DoctorService;
import com.vheal.service.PatientService;
import com.vheal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
@Component
public class LoginController {

    private UserRepository userRepo;
    private RoleRepository roleRepo;
    private UserService userService;
    private DoctorService doctorService;
    private PatientService patientService;


    @Autowired
    public LoginController(UserRepository userRepo, RoleRepository roleRepo, UserService userService,
                           PatientService patientService, DoctorService doctorService) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.userService = userService;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("role",new Role());
        return "signup_form";
    }

    @PostMapping("/processRegister")
    public String processRegister(@ModelAttribute("role") Role role, @ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model theModel){

        // handle errors
        if(bindingResult.hasErrors()){
            return "signup_form";
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepo.save(user);
        Role theRole = roleRepo.findByName(role.getName());
        user.addRole(theRole);
        roleRepo.save(theRole);
        // Register new Doctor
        if(theRole.getName().contains("DOCTOR")){
            // create new Doctor and add model attribute to form
            Doctor theDoctor = new Doctor();
            // join doctor with user entity
            theDoctor.setUser(user);
            theModel.addAttribute("doctor", theDoctor);
            return "doctors/doctor-form";
        }

        // Register new Patient
        if(theRole.getName().contains("PATIENT")){
            // create new Patient and add model attribute to form
            Patient thePatient = new Patient();
            // join patient with user entity
            thePatient.setUser(user);
            theModel.addAttribute("patient", thePatient);
            return "patients/patient-form";
        }

        return "signup_form";
    }

    @GetMapping("/login")
    public String login() {
        User user = getPrincipal();
        if (user != null){
            return "processLogin";
        }
        return "login";
    }

    private User getPrincipal(){
        User user = null;
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User){
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return user;
    }

    @GetMapping("/processLogin")
    public String processLogin(Model model){

        User theUser = userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        if(theUser.getRole().getName().contains("DOCTOR")){
            Doctor theDoctor = theUser.getDoctor();
            model.addAttribute("doctorPatients", theDoctor.getPatients());
            model.addAttribute("doctor", theDoctor);
            return "doctors/doctor-page";
        }else if(theUser.getRole().getName().contains("PATIENT")){
            Patient thePatient = theUser.getPatient();
            if(thePatient.getDoctor() == null){
                model.addAttribute("patient", thePatient);
                return "patients/patientX-page";
            }
            model.addAttribute("doctor", thePatient.getDoctor());
            model.addAttribute("patientPrescriptions", thePatient.getPrescriptions());
            model.addAttribute("patient", thePatient);
            return "patients/patient-page";
        }else{
            return "/";
        }
    }


    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> listUsers = userRepo.findAll();
        model.addAttribute("listUsers", listUsers);

        return "users";
    }

    @GetMapping("/access-denied")
    public String showAccessDenied() {
        return "access-denied";
    }
}
