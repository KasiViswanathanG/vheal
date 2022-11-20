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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

//Controller for Doctor and Drug relations
@Controller
@RequestMapping("/doctors")
public class DoctorDrugController {

    private DoctorService doctorService;
    private PatientService patientService;
    private PrescriptionService prescriptionService;
    private DrugService drugService;

    //    constructor injection of service
    @Autowired
    public DoctorDrugController(DoctorService theDoctorService, PatientService thePatientService,
                                        PrescriptionService thePrescriptionService, DrugService theDrugService) {
        this.doctorService = theDoctorService;
        this.patientService = thePatientService;
        this.prescriptionService = thePrescriptionService;
        this.drugService = theDrugService;
    }


    // mapping to search and add drugs
    @GetMapping("/searchDrug")
    public String search(@RequestParam("patientId") int thePatientId, @RequestParam("doctorId") int theDoctorId,
                         @RequestParam("id") int prescriptionId, @RequestParam("drugSearch") String theDrugSearch,
                         Model theModel) {

        // doctor and patient model to form
        Patient thePatient = patientService.findById(thePatientId);
        Doctor theDoctor = doctorService.findById(theDoctorId);
        theModel.addAttribute("patient",thePatient);
        theModel.addAttribute("doctor",theDoctor);

        // prescription and prescribedDrugs model to form
        Prescription thePrescription = prescriptionService.findById(prescriptionId);
        theModel.addAttribute("prescription", thePrescription);
        List<Drug> drugList = thePrescription.getDrugs();
        theModel.addAttribute("prescribedDrugs", drugList);

        //  if Drug name is empty return to prescription
        if (theDrugSearch.trim().isEmpty()) {
            return "prescriptions/prescription-form";
        } else {
            // else, search by Drug name
            List<Drug> result = new ArrayList<>();
            for (Drug drugs : drugService.findAll()) {
                if (drugs.getDrugName().toLowerCase().contains(theDrugSearch.toLowerCase())) {
                    result.add(drugs);
                }
            }
            // add drugs spring model
            theModel.addAttribute("drugs", result);
            return "prescriptions/prescription-form";
        }
    }

    // mapping to add drugs to patient's prescription
    @GetMapping("/addDrug")
    public String addDrug(@RequestParam("patientId") int thePatientId, @RequestParam("doctorId") int theDoctorId,
                          @RequestParam("drugId") int theDrugId, @RequestParam("prescriptionId") int thePrescriptionId,
                          Model theModel){

        // doctor and patient model to form
        Patient thePatient = patientService.findById(thePatientId);
        Doctor theDoctor = doctorService.findById(theDoctorId);
        theModel.addAttribute("patient",thePatient);
        theModel.addAttribute("doctor",theDoctor);

        Drug theDrug = drugService.findById(theDrugId);
        theModel.addAttribute("drug",theDrug);
        Prescription thePrescription = prescriptionService.findById(thePrescriptionId);
        List<Drug> drugList = thePrescription.getDrugs();

        // add drug to prescription if not already prescribed
        if(!hasDrug(drugList, theDrug)){

            // use convenience method to add drug
            thePrescription.addDrug(theDrug);
            drugService.save(theDrug);
            prescriptionService.save(thePrescription);
        }
        theModel.addAttribute("prescribedDrugs", drugList);
        theModel.addAttribute("prescription", thePrescription);
        return "prescriptions/prescription-form";
    }

    public boolean hasDrug(List<Drug> drugList, Drug theDrug){
        for (Drug tempDrug: drugList) {
            if(tempDrug.getId() == theDrug.getId()){
                return true;
            }
        }
        return false;
    }

    // mapping to delete drugs from patient's prescription
    @GetMapping("/deleteDrug")
    public String deleteDrug(@RequestParam("patientId") int thePatientId, @RequestParam("doctorId") int theDoctorId,
                             @RequestParam("prescribedDrugId") int theDrugId, @RequestParam("prescriptionId") int thePrescriptionId,
                             Model theModel){

        // doctor and patient model to form
        Patient thePatient = patientService.findById(thePatientId);
        Doctor theDoctor = doctorService.findById(theDoctorId);
        theModel.addAttribute("patient",thePatient);
        theModel.addAttribute("doctor",theDoctor);

        Drug theDrug = drugService.findById(theDrugId);
        Prescription thePrescription = prescriptionService.findById(thePrescriptionId);

        // use convenience method to delete drug
        thePrescription.deleteDrug(theDrug);
        Drug updatedDrug = drugService.findById(theDrugId);
        drugService.save(updatedDrug);

        // return updated prescription
        Prescription updatedPrescription = prescriptionService.findById(thePrescriptionId);
        prescriptionService.save(updatedPrescription);
        List<Drug> drugList = updatedPrescription.getDrugs();
        theModel.addAttribute("drug",updatedDrug);
        theModel.addAttribute("prescribedDrugs", drugList);
        theModel.addAttribute("prescription", updatedPrescription);
        return "prescriptions/prescription-form";
    }

}
