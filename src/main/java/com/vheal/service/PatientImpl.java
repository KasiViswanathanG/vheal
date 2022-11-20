package com.vheal.service;

import com.vheal.dao.PatientRepository;
import com.vheal.entity.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//Implementation for Patient service
@Service
public class PatientImpl implements PatientService{

    private PatientRepository patientRepository;

    // constructor dependency injection
    @Autowired
    public PatientImpl(PatientRepository thePatientRepository){
        patientRepository = thePatientRepository;
    }

    @Override
    public List<Patient> findAll() {
        return patientRepository.findAllByOrderByPatientNameAsc();
    }

    @Override
    public Patient findById(int theId) {
        Optional<Patient> result = patientRepository.findById(theId);

        Patient thePatient = null;

        if (result.isPresent()) {
            thePatient = result.get();
        }
        else {
            // if we didn't find the patient return null
            return thePatient;
        }
//        System.out.println(thePatient);
        return thePatient;
    }

    @Override
    public void save(Patient thePatient) {
        patientRepository.save(thePatient);
    }

    @Override
    public void deleteById(int theId) {
        patientRepository.deleteById(theId);
    }

}
