package com.vheal.service;

import com.vheal.dao.DoctorRepository;
import com.vheal.entity.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Implementation for Doctor service
@Service
public class DoctorImpl implements DoctorService{

    private DoctorRepository doctorRepository;

//    constructor dependency injection
    @Autowired
    public DoctorImpl(DoctorRepository theDoctorRepository){
        doctorRepository = theDoctorRepository;
    }

    @Override
    public List<Doctor> findAll() {
        return doctorRepository.findAllByOrderByDoctorNameAsc();
    }

    @Override
    public Doctor findById(int theId) {
        Optional<Doctor> result = doctorRepository.findById(theId);

        Doctor theDoctor = null;

        if (result.isPresent()) {
            theDoctor = result.get();
        }
        else {
            // we didn't find the Doctor return null
            return theDoctor;
        }
        return theDoctor;
    }

    @Override
    public void save(Doctor theDoctor) {
        doctorRepository.save(theDoctor);
    }

    @Override
    public void deleteById(int theId) {
        doctorRepository.deleteById(theId);
    }

}
