package com.vheal.dao;

import com.vheal.entity.Drug;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//CRUD operations for drug entity
public interface DrugRepository extends JpaRepository<Drug, Integer> {

    //  method to sort and find all by Drug name
    public List<Drug> findAllByOrderByDrugNameAsc();

    //  method to find by Type
    public Drug findByTypeStartingWith(String theDrug);

    public Drug findByDrugName(String drugName);
}
