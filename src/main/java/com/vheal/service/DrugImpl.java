package com.vheal.service;

import com.vheal.dao.DrugRepository;
import com.vheal.entity.Drug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Implementation for Drug service
@Service
public class DrugImpl implements DrugService{

    private DrugRepository drugRepository;

    // constructor dependency injection
    @Autowired
    public DrugImpl(DrugRepository theDrugRepository){
        drugRepository = theDrugRepository;
    }

    @Override
    public List<Drug> findAll() {
        return drugRepository.findAllByOrderByDrugNameAsc();
    }

    @Override
    public Drug findById(int theId) {
        Optional<Drug> result = drugRepository.findById(theId);

        Drug theDrug = null;

        if (result.isPresent()) {
            theDrug = result.get();
        }
        else {
            // we didn't find the drug return null
            return theDrug;
        }
        return theDrug;
    }

    @Override
    public void save(Drug theDrug) {
        drugRepository.save(theDrug);
    }

    @Override
    public void deleteById(int theId) {
        drugRepository.deleteById(theId);
    }

    @Override
    public Drug findDrug(String theDrug) {
        return drugRepository.findByTypeStartingWith(theDrug);
    }
}
