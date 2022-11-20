package com.vheal.controller;


import com.vheal.entity.Doctor;
import com.vheal.entity.Drug;
import com.vheal.entity.Patient;
import com.vheal.entity.Prescription;
import com.vheal.service.DoctorService;
import com.vheal.service.DrugService;
import com.vheal.service.PatientService;
import com.vheal.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//Controller for Doctor pages
@Component
@Controller
@RequestMapping("/doctors")
public class DoctorController {

    private DoctorService doctorService;
    private PatientService patientService;
    private PrescriptionService prescriptionService;
    private DrugService drugService;

    //    constructor injection of service
    @Autowired
    public DoctorController(DoctorService theDoctorService, PatientService thePatientService,
                            PrescriptionService thePrescriptionService, DrugService theDrugService) {
        this.doctorService = theDoctorService;
        this.patientService = thePatientService;
        this.prescriptionService = thePrescriptionService;
        this.drugService = theDrugService;
    }

    // mapping to save Doctor details
    @PostMapping("/saveDoctor")
    public String saveDoctor(@ModelAttribute("doctor") @Valid Doctor theDoctor, BindingResult bindingResult, Model theModel){

        // handle errors
        if(bindingResult.hasErrors()){
            return "doctors/doctor-form";
        }else {

            if(doctorService.findById(theDoctor.getId()) != null){
                doctorService.save(theDoctor);
                System.out.println(theDoctor.getPatients());
                theModel.addAttribute("doctor",theDoctor);
                theModel.addAttribute("doctorPatients", theDoctor.getPatients());
                return "doctors/doctor-page";
            }
            // save new Doctor
            doctorService.save(theDoctor);

            // redirect to success
            return "register_success";
        }
    }

    // mapping to modify Doctor details
    @GetMapping("/showFormForUpdateDoctor")
    public String showFormForUpdateDoctor(@RequestParam("doctorId") int theId, Model theModel){

        // get Doctor from service
        Doctor theDoctor = doctorService.findById(theId);

        // set doctor as model attribute to pre-populate form
        theModel.addAttribute("doctor", theDoctor);

        return "doctors/doctor-form";
    }

    @GetMapping("/doctors/showFormForUpdateDoctor")
    public String showFormForUpdateDoctors(@RequestParam("doctorId") int theId, Model theModel){

        // get Doctor from service
        Doctor theDoctor = doctorService.findById(theId);

        // set doctor as model attribute to pre-populate form
        theModel.addAttribute("doctor", theDoctor);

        return "doctors/doctor-form";
    }

    // mapping to display Doctor's UI page
    @GetMapping("/showDoctor")
    public String showDoctor( @RequestParam("doctorId") int theDoctorId, Model theModel){
        Doctor theDoctor = doctorService.findById(theDoctorId);
        theModel.addAttribute("doctorPatients", theDoctor.getPatients());
        theModel.addAttribute("doctor", theDoctor);
        return "doctors/doctor-page";
    }


}
