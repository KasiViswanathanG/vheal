package com.vheal.dao;

import com.vheal.entity.Drug;
import com.vheal.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//CRUD operations for prescription entity
public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {

    // method to sort and find all by prescriptions
    public List<Prescription> findAllByOrderBySuggestionAsc();

    public Prescription findByDate(String date);
}
