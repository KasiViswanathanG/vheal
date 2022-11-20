package com.vheal.dao;

import com.vheal.entity.Doctor;
import com.vheal.entity.Drug;
import com.vheal.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//CRUD operations for Doctor entity
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    // method to sort and find all by Doctor name
    public List<Doctor> findAllByOrderByDoctorNameAsc();

    public Doctor findByDoctorName(String doctorName);
}
