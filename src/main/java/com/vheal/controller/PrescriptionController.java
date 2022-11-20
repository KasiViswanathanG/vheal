package com.vheal.controller;

import com.vheal.entity.Doctor;
import com.vheal.entity.Drug;
import com.vheal.entity.Patient;
import com.vheal.entity.Prescription;
import com.vheal.service.DoctorService;
import com.vheal.service.DrugService;
import com.vheal.service.PatientService;
import com.vheal.service.PrescriptionService;
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

//Controller for Prescription page
@Controller
@RequestMapping("/prescriptions")
public class PrescriptionController {
    private PrescriptionService prescriptionService;
    private PatientService patientService;
    private DoctorService doctorService;
    private DrugService drugService;

    public PrescriptionController(PrescriptionService prescriptionService, PatientService patientService, DoctorService doctorService) {
        this.prescriptionService = prescriptionService;
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    @GetMapping("/showPatientPrescription")
    public String showPrescription(@RequestParam("patientId") int thePatientId,@RequestParam("doctorId") int theDoctorId,
                                   @RequestParam("prescriptionId") int thePrescriptionId,
                                    Model theModel){
        Patient thePatient = patientService.findById(thePatientId);
        Prescription thePrescription = prescriptionService.findById(thePrescriptionId);
        Doctor theDoctor = doctorService.findById(theDoctorId);
        theModel.addAttribute("patient",thePatient);
        theModel.addAttribute("prescription",thePrescription);
        theModel.addAttribute("doctor",theDoctor);
        theModel.addAttribute("prescribedDrugs",thePrescription.getDrugs());
        return "prescriptions/prescription-form";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("patientId") int thePatientId,@RequestParam("doctorId") int theDoctorId,
                                    @RequestParam("prescriptionId") int thePrescriptionId, Model theModel){

        Patient thePatient = patientService.findById(thePatientId);
        Doctor theDoctor = doctorService.findById(theDoctorId);
        Prescription thePrescription = prescriptionService.findById(thePrescriptionId);
        thePrescription.setSuggestion(null);
        // return updated prescription to the form
        theModel.addAttribute("patient",thePatient);
        theModel.addAttribute("doctor",theDoctor);
        theModel.addAttribute("prescription", thePrescription);
        theModel.addAttribute("prescribedDrugs",thePrescription.getDrugs());

        return "prescriptions/prescription-form";
    }

}
