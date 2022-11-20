package com.vheal.controller;

import com.vheal.entity.Drug;
import com.vheal.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

//Controller for Drug page
@Controller
@RequestMapping("/drugs")
@Component
public class DrugController {

    private DrugService drugService;

    // constructor injection of service
    @Autowired
    public DrugController(DrugService theDrugService) {
        this.drugService = theDrugService;
    }


    // mapping to display Drugs
    @GetMapping("/list")
    public String listDrugs(Model theModel){

        // get all drugs from database
        List<Drug> theDrugs = drugService.findAll();

        // add to spring model
        theModel.addAttribute("drugs", theDrugs);

        return "drugs/list-drugs";
    }

    //    mapping to add new Drug
    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel){

        // create new drug
        Drug theDrug = new Drug();

        // add to spring model
        theModel.addAttribute("drug", theDrug);

        return "drugs/drug-form";
    }

    //    mapping to modify Drug
    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("drugId") int theId, Model theModel){

        //  get drug from service
        Drug theDrug = drugService.findById(theId);

        //  set drug as model attribute to pre-populate form
        theModel.addAttribute("drug", theDrug);

        return "drugs/drug-form";
    }

    // mapping to add drug
    @PostMapping("/save")
    public String saveDrug(@ModelAttribute("drug") @Valid Drug theDrug, BindingResult bindingResult){

        // handle errors
        if(bindingResult.hasErrors()){
            return "drugs/drug-form";
        }else {
            // save drug
            drugService.save(theDrug);

            // redirect to prevent duplicate submissions
            return "redirect:/drugs/list";
        }
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("drugId") int theId){

        // delete drug
        drugService.deleteById(theId);

        // redirect to Drugs list
        return "redirect:/drugs/list";
    }

    @GetMapping("/search")
    public String search(@RequestParam("drugSearch") String theDrugSearch, Model theModel) {

        // check Drug name & type if both are empty then just give list of all drugs
        if (theDrugSearch.trim().isEmpty()) {
            return "redirect:/drugs/list";
        } else {
            // else, search by Drug name and Type
            List<Drug> result = new ArrayList<>();
            for (Drug drugs : drugService.findAll()) {
                if (drugs.getDrugName().toLowerCase().contains(theDrugSearch.toLowerCase())
                        || drugs.getType().toLowerCase().contains(theDrugSearch.toLowerCase())) {
                    result.add(drugs);
                }
            }
            // add to spring model
            theModel.addAttribute("drugs", result);
            return "drugs/list-drugs";
        }
    }

}
