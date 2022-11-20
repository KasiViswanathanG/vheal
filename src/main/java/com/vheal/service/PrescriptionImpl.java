package com.vheal.service;


import com.vheal.dao.PrescriptionRepository;
import com.vheal.entity.Prescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Implementation for Drug service
@Service
public class PrescriptionImpl implements PrescriptionService{

    private PrescriptionRepository prescriptionRepository;

    // constructor dependency injection
    @Autowired
    public PrescriptionImpl(PrescriptionRepository thePrescriptionRepository){
        prescriptionRepository = thePrescriptionRepository;
    }

    @Override
    public List<Prescription> findAll() {
        return prescriptionRepository.findAllByOrderBySuggestionAsc();
    }

    @Override
    public Prescription findById(int theId) {
        Optional<Prescription> result = prescriptionRepository.findById(theId);

        Prescription thePrescription = null;

        if (result.isPresent()) {
            thePrescription = result.get();
        }
        else {
            // if we didn't find the Prescription return null
            return thePrescription;
        }
        return thePrescription;
    }

    @Override
    public void save(Prescription thePrescription) {
        prescriptionRepository.save(thePrescription);
    }

    @Override
    public void deleteById(int theId) {
        prescriptionRepository.deleteById(theId);
    }

}
