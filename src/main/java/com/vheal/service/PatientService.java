package com.vheal.service;


import com.vheal.entity.Patient;

import java.util.List;

//Service layer for Patient entity
public interface PatientService {

    public List<Patient> findAll();

    public Patient findById(int theId);

    public void save(Patient thePatient);

    public void deleteById(int theId);
}
