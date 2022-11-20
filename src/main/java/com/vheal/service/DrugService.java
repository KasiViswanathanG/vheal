package com.vheal.service;


import com.vheal.entity.Drug;

import java.util.List;

//Service layer for Drug entity
public interface DrugService {

    public List<Drug> findAll();

    public Drug findById(int theId);

    public void save(Drug theDrug);

    public void deleteById(int theId);

    public Drug findDrug(String theDrug);
}
