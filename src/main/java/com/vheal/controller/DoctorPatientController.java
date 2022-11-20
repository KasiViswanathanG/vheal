package com.vheal.controller;

import com.vheal.entity.Doctor;
import com.vheal.entity.Patient;
import com.vheal.service.DoctorService;
import com.vheal.service.DrugService;
import com.vheal.service.PatientService;
import com.vheal.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

//Controller for Doctor and Patient relations
@Controller
@RequestMapping("/doctors")
public class DoctorPatientController {

    private DoctorService doctorService;
    private PatientService patientService;
    private PrescriptionService prescriptionService;
    private DrugService drugService;

    //    constructor injection of service
    @Autowired
    public DoctorPatientController(DoctorService theDoctorService, PatientService thePatientService,
                            PrescriptionService thePrescriptionService, DrugService theDrugService) {
        this.doctorService = theDoctorService;
        this.patientService = thePatientService;
        this.prescriptionService = thePrescriptionService;
        this.drugService = theDrugService;
    }

    // mapping to search patients for doctor
    @GetMapping("/searchPatients")
    public String searchPatients(@RequestParam("id") int doctorId, @RequestParam("patientSearch") String thePatientSearch, Model theModel) {

        Doctor theDoctor = doctorService.findById(doctorId);
        theModel.addAttribute("doctor", theDoctor);
        List<Patient> patientList = theDoctor.getPatients();
        theModel.addAttribute("doctorPatients",patientList);
        List<Patient> result = new ArrayList<>();
        //  if Patient name is empty return to prescription
        if (thePatientSearch.trim().isEmpty()) {
            return "doctors/doctor-page";
        } else {
            // else, search by Patient name
            for (Patient patient : patientService.findAll()) {
                if (patient.getPatientName().toLowerCase().contains(thePatientSearch.toLowerCase())) {
                    result.add(patient);
                }
            }
            // add drugs spring model
            theModel.addAttribute("patients", result);
            return "doctors/doctor-page";
        }
    }

    // mapping to add patients for doctor
    @GetMapping("/addDoctorPatient")
    public String addDoctorPatient(@RequestParam("patientId") int thePatientId, @RequestParam("doctorId") int theDoctorId, Model theModel){
        Patient thePatient = patientService.findById(thePatientId);
        Doctor theDoctor = doctorService.findById(theDoctorId);
        List<Patient> patientList = theDoctor.getPatients();

        // add patient to doctor if not already prescribed
        if(!hasPatient(patientList, thePatient)){
            // use convenience method to add drug
            theDoctor.addPatient(thePatient);
            patientService.save(thePatient);
            doctorService.save(theDoctor);
        }
        theModel.addAttribute("doctorPatients", patientList);
        theModel.addAttribute("doctor", theDoctor);
        return "doctors/doctor-page";
    }

    public boolean hasPatient(List<Patient> patientList, Patient thePatient){
        for (Patient tempPatient: patientList) {
            if(tempPatient.getId() == thePatient.getId()){
                return true;
            }
        }
        return false;
    }

    // mapping to remove patients for doctor
    @GetMapping("/deleteDoctorPatient")
    public String deletePatient(@RequestParam("doctorPatientId") int thePatientId, @RequestParam("doctorId") int theDoctorId, Model theModel){
        Patient thePatient = patientService.findById(thePatientId);
        Doctor theDoctor = doctorService.findById(theDoctorId);

        // use convenience method to delete drug
        theDoctor.deletePatient(thePatient);
        Patient updatedPatient = patientService.findById(thePatientId);
        patientService.save(updatedPatient);

        // return updated Doctor
        Doctor updatedDoctor = doctorService.findById(theDoctorId);
        doctorService.save(updatedDoctor);
        List<Patient> patientList = updatedDoctor.getPatients();
        theModel.addAttribute("doctorPatients", patientList);
        theModel.addAttribute("doctor", updatedDoctor);
        return "doctors/doctor-page";
    }

    // mapping to search patients of doctor
    @GetMapping("/searchDoctorPatients")
    public String searchDoctorPatients(@RequestParam("id") int doctorId, @RequestParam("doctorPatientSearch") String thePatientSearch, Model theModel) {

        Doctor theDoctor = doctorService.findById(doctorId);
        theModel.addAttribute("doctor", theDoctor);
        List<Patient> result = new ArrayList<>();
        // search by Patient name
        for (Patient patient : theDoctor.getPatients()) {
            if (patient.getPatientName().toLowerCase().contains(thePatientSearch.toLowerCase())) {
                result.add(patient);
            }
        }
        // add drugs spring model
        theModel.addAttribute("doctorPatients", result);
        return "doctors/doctor-page";
    }

    // mapping to update patient details
    @GetMapping("/patients/showFormForUpdatePatient")
    public String showFormForUpdatePatients(@RequestParam("patientId") int theId, Model theModel){

        // get Patient from service
        Patient thePatient = patientService.findById(theId);

        // set patient as model attribute to pre-populate form
        theModel.addAttribute("patient", thePatient);

        return "patients/patient-form";
    }

    // mapping to view Doctor's Patient
    @GetMapping("/showPatient")
    public String showPatient(@RequestParam("doctorPatientId") int thePatientId, @RequestParam("doctorId") int theDoctorId, Model theModel){
        Patient thePatient = patientService.findById(thePatientId);
        Doctor theDoctor = doctorService.findById(theDoctorId);
        theModel.addAttribute("patient", thePatient);
        theModel.addAttribute("doctor", theDoctor);
        theModel.addAttribute("patientPrescriptions",thePatient.getPrescriptions());
        return "patients/patient-page";
    }

}
