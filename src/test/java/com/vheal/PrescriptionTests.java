package com.vheal;

import com.vheal.dao.DrugRepository;
import com.vheal.dao.PrescriptionRepository;
import com.vheal.entity.Drug;
import com.vheal.entity.Patient;
import com.vheal.entity.Prescription;
import com.vheal.service.PatientImpl;
import com.vheal.service.PatientService;
import com.vheal.service.PrescriptionImpl;
import com.vheal.service.PrescriptionService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PrescriptionTests {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private DrugRepository drugRepository;

    @Test
    @Rollback(value = false)
    @Order(1)
    public void testCreatePrescription(){
        Prescription prescription = new Prescription("Drink water a lot","05-05-2020");
        Prescription savedPrescription = prescriptionRepository.save(prescription);
        assertNotNull(savedPrescription);
    }

    @Test
    @Order(2)
    public void testFindPrescriptionByDateExist(){
        String prescriptionDate = "05-05-2020";
        Prescription prescription = prescriptionRepository.findByDate(prescriptionDate);
        assertThat(prescription.getDate()).isEqualTo(prescriptionDate);
    }

    @Test
    @Order(3)
    public void testFindPrescriptionByIdNotExist(){
        String prescriptionDate = "05-05-2030";
        Prescription prescription = prescriptionRepository.findByDate(prescriptionDate);
        assertNull(prescription);
    }

    @Test
    @Order(4)
    public void testUpdatePrescription(){
        String date = "05-05-2022";
        Prescription prescription = new Prescription("Drink lot of water",date);
        prescription.setSuggestion("Drink 5L of water daily");
        prescriptionRepository.save(prescription);
        Prescription updatedPrescription= prescriptionRepository.findByDate(date);
        assertThat(updatedPrescription.getDate()).isEqualTo(date);
    }

    @Test
    @Rollback(value = false)
    @Order(5)
    public void testListPrescription(){
        List<Prescription> prescriptions = prescriptionRepository.findAll();

        for (Prescription prescription : prescriptions){
            System.out.println(prescription);
        }
        assertThat(prescriptions).size().isGreaterThan(0);
    }

    @Test
    @Rollback(value = false)
    @Order(6)
    public void testDeletePrescription(){
        String prescriptionDate = "05-05-2020";
        Prescription prescription = prescriptionRepository.findByDate(prescriptionDate);
        Integer id = prescription.getId();
        boolean isExistBeforeDelete = prescriptionRepository.findById(id).isPresent();
        prescriptionRepository.deleteById(id);
        boolean notExistAfterDelete = prescriptionRepository.findById(id).isPresent();
        assertTrue(isExistBeforeDelete);
        assertFalse(notExistAfterDelete);
    }

    @Test
    @Order(7)
    public void testCreatePrescriptionWithId(){
        Prescription prescription = new Prescription(100,"Drink water a lot","05-05-2020");
        Prescription savedPrescription = prescriptionRepository.save(prescription);
        assertNotNull(savedPrescription);
    }

    @Test
    @Order(8)
    public void testCreatePrescriptionWithGetterSetter(){
        Prescription prescription = new Prescription(100,"Drink water a lot","05-05-2020");
        prescription.setId(100);
        prescription.getId();
        prescription.setSuggestion("Drink water a lot");
        prescription.getSuggestion();
        prescription.setDate("05-05-2020");
        prescription.getDate();
        Drug drug = drugRepository.getById(1);
        prescription.addDrug(drug);
        prescription.deleteDrug(drug);
        prescription.setDrugs(drugRepository.findAll());
        prescription.getDrugs();
        Drug notExistDrug = new Drug();
        prescription.addDrug(notExistDrug);
        prescription.deleteDrug(notExistDrug);
    }

    @Test
    @Order(9)
    public void testPrescriptionService() {
        PrescriptionService prescriptionService = new PrescriptionImpl(prescriptionRepository);
        prescriptionService.findAll();
        prescriptionService.findById(1);
        prescriptionService.findById(100);
        Prescription prescription = new Prescription("Drink water a lot","05-05-2020");
        prescriptionService.save(prescription);
        prescriptionService.deleteById(1);
    }
}
