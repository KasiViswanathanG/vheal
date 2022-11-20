package com.vheal.service;


import com.vheal.entity.Doctor;

import java.util.List;

//Service layer for Doctor entity
public interface DoctorService {

    public List<Doctor> findAll();

    public Doctor findById(int theId);

    public void save(Doctor theDoctor);

    public void deleteById(int theId);
}
