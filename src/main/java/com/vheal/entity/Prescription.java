package com.vheal.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//create table & sync to prescription entity in sql workbench
@Entity
@Table(name="prescription")
public class Prescription {

    // define fields for drug

    // primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "suggestion")
    private String suggestion;

    @Column(name = "date")
    private String date;

    // many to many relationship with drugs
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "prescription_drug",
            joinColumns = @JoinColumn(name = "prescription_id"),
            inverseJoinColumns = @JoinColumn(name = "drug_id"))
    private List<Drug> drugs;


    // define constructors
    public Prescription(){}

    public Prescription(int id, String suggestion, String date) {
        this.id = id;
        this.suggestion = suggestion;
        this.date = date;
    }

    public Prescription(String suggestion, String date) {
        this.suggestion = suggestion;
        this.date = date;
    }


    // define getters/setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Drug> getDrugs() {
        return drugs;
    }

    public void setDrugs(List<Drug> drugs) {
        this.drugs = drugs;
    }

    // add convenience method
    public void addDrug(Drug theDrug){
        if(drugs == null){
            drugs = new ArrayList<>();
        }
        drugs.add(theDrug);
    }

    public void deleteDrug(Drug theDrug) {
        List<Drug> newDrugs = new ArrayList<>();
        for (Drug tempDrug: drugs) {
            if(tempDrug.getId() != theDrug.getId()){
                newDrugs.add(tempDrug);
            }
        }
        this.drugs = newDrugs;
    }


    // define toString
    @Override
    public String toString() {
        return "Prescription{" +
                "id=" + id +
                ", suggestion='" + suggestion + '\'' +
                ", date='" + date + '\'' +
                ", drugs='" + drugs + '\'' +
                '}';
    }
}
