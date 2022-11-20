package com.vheal.service;


import com.vheal.entity.Prescription;

import java.util.List;

//Service layer for Prescription entity
public interface PrescriptionService {

    public List<Prescription> findAll();

    public Prescription findById(int theId);

    public void save(Prescription thePrescription);

    public void deleteById(int theId);
}
