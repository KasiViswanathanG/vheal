package com.vheal.dao;

import com.vheal.entity.Doctor;
import com.vheal.entity.Patient;
import com.vheal.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//CRUD operations for Patient entity
public interface PatientRepository extends JpaRepository<Patient, Integer> {

//    method to sort by Patient name
    public List<Patient> findAllByOrderByPatientNameAsc();

    public Patient findByPatientName(String patientName);
}
