package com.vheal.controller;

import com.vheal.entity.Doctor;
import com.vheal.entity.Patient;
import com.vheal.entity.Prescription;
import com.vheal.service.DoctorService;
import com.vheal.service.DrugService;
import com.vheal.service.PatientService;
import com.vheal.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

//Controller for Doctor and Prescription relations
@Controller
@RequestMapping("/doctors")
public class DoctorPrescriptionController {

    private DoctorService doctorService;
    private PatientService patientService;
    private PrescriptionService prescriptionService;
    private DrugService drugService;

    //    constructor injection of service
    @Autowired
    public DoctorPrescriptionController(DoctorService theDoctorService, PatientService thePatientService,
                            PrescriptionService thePrescriptionService, DrugService theDrugService) {
        this.doctorService = theDoctorService;
        this.patientService = thePatientService;
        this.prescriptionService = thePrescriptionService;
        this.drugService = theDrugService;
    }

    // mapping to add new Prescription
    @GetMapping("/showFormForAddPrescription")
    public String showFormForAddPrescription(@RequestParam("patientId") int thePatientId, @RequestParam("doctorId") int theDoctorId, Model theModel){
        // create new Prescription
        Prescription thePrescription = new Prescription();
        prescriptionService.save(thePrescription);
        theModel.addAttribute("prescription", thePrescription);

        Patient thePatient = patientService.findById(thePatientId);
        Doctor theDoctor = doctorService.findById(theDoctorId);
        theModel.addAttribute("patient", thePatient);
        theModel.addAttribute("doctor", theDoctor);
        return "prescriptions/prescription-form";
    }

    // mapping to delete patient's prescription
    @GetMapping("/deletePrescription")
    public String deletePrescription(@RequestParam("patientId") int thePatientId, @RequestParam("prescriptionId") int thePrescriptionId, @RequestParam("doctorId") int theDoctorId, Model theModel){

        // delete prescription and update patient
        Patient thePatient = patientService.findById(thePatientId);
        prescriptionService.deleteById(thePrescriptionId);
        patientService.save(thePatient);
        // redirect to prevent duplicate submissions
        Doctor theDoctor = doctorService.findById(theDoctorId);
        theModel.addAttribute("doctor",theDoctor);
        theModel.addAttribute("patient",thePatient);
        theModel.addAttribute("patientPrescriptions",thePatient.getPrescriptions());
        return "patients/patient-page";
    }

    // mapping to save new or updated Prescription
    @PostMapping("/suggestion")
    public String suggestion(@RequestParam("patientId") int thePatientId, @RequestParam("doctorId") int theDoctorId,
                             @ModelAttribute("prescription") Prescription thePrescription,
                             BindingResult bindingResult, Model theModel) {
        //        handle errors
        if (bindingResult.hasErrors()) {
            return "prescriptions/prescription-form";
        } else {
            // doctor and patient model to form
            Patient thePatient = patientService.findById(thePatientId);
            Doctor theDoctor = doctorService.findById(theDoctorId);
            theModel.addAttribute("patient",thePatient);
            theModel.addAttribute("doctor",theDoctor);

            //set current date
            LocalDateTime ldt = LocalDateTime.now();
            thePrescription.setDate(DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH).format(ldt));
            if(thePrescription.getDrugs().isEmpty() == false){
                thePrescription.setDrugs(thePrescription.getDrugs());
                thePrescription.setSuggestion(thePrescription.getSuggestion());
                // save prescription
                prescriptionService.save(thePrescription);
            }else {
                // add prescription to patient
                thePatient.addPrescription(thePrescription);
                // save prescription and patient
                patientService.save(thePatient);
                prescriptionService.save(thePrescription);
            }
            theModel.addAttribute("prescription", thePrescription);
            theModel.addAttribute("prescribedDrugs",thePrescription.getDrugs());
            // redirect to prevent duplicate submissions
            return "prescriptions/prescription-form";
        }
    }
}
