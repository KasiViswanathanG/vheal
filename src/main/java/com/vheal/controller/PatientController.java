package com.vheal.controller;

import com.vheal.entity.Doctor;
import com.vheal.entity.Patient;
import com.vheal.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/patients")
public class PatientController {

    private DoctorService doctorService;
    private PatientService patientService;
    private PrescriptionService prescriptionService;

    //    constructor injection of service
    @Autowired
    public PatientController(DoctorService theDoctorService, PatientService thePatientService,
                             PrescriptionService thePrescriptionService) {
        this.doctorService = theDoctorService;
        this.patientService = thePatientService;
        this.prescriptionService = thePrescriptionService;
    }


    // mapping to save Patient
    @PostMapping("/savePatient")
    public String savePatient(@ModelAttribute("patient") @Valid Patient thePatient, BindingResult bindingResult, Model theModel) {

        // handle errors
        if (bindingResult.hasErrors()) {
            return "patients/patient-form";
        } else {

            if(patientService.findById(thePatient.getId()) != null){
                patientService.save(thePatient);
                theModel.addAttribute("patientPrescriptions", thePatient.getPrescriptions());
                theModel.addAttribute("patient", thePatient);
                theModel.addAttribute("doctor",thePatient.getDoctor());
                return "patients/patient-page";
            }
            // save new Patient
            patientService.save(thePatient);

            // redirect to success
            return "register_success";
        }
    }

    // mapping to modify Doctor
    @GetMapping("/showFormForUpdatePatient")
    public String showFormForUpdatePatient(@RequestParam("patientId") int theId, Model theModel){

        // get Patient from service
        Patient thePatient = patientService.findById(theId);

        // set patient as model attribute to pre-populate form
        theModel.addAttribute("patient", thePatient);

        return "patients/patient-form";
    }

    @GetMapping("/patients/showFormForUpdatePatient")
    public String showFormForUpdatePatients(@RequestParam("patientId") int theId, Model theModel){

        // get Patient from service
        Patient thePatient = patientService.findById(theId);

        // set patient as model attribute to pre-populate form
        theModel.addAttribute("patient", thePatient);

        return "patients/patient-form";
    }

    // mapping to display Patient's UI page
    @GetMapping("/showPatient")
    public String showPatient(@ModelAttribute("patient") Patient thePatient, Model theModel){
        theModel.addAttribute("patientPrescriptions", thePatient.getPrescriptions());
        theModel.addAttribute("patient", thePatient);
        return "patients/patient-page";
    }
}
