package com.vheal.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

//create table & sync to drug entity in sql workbench
@Entity
@Table(name="drug")
public class Drug {

    // fields for drug

    // primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank(message = "is required")
    @Column(name = "drug_name")
    private String drugName;

    @NotBlank(message = "is required")
    @Column(name = "type")
    private String type;

    // many to many relationship with prescriptions
    @ManyToMany(fetch=FetchType.LAZY,
            cascade= {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "prescription_drug",
            joinColumns = @JoinColumn(name = "drug_id"),
            inverseJoinColumns = @JoinColumn(name = "prescription_id")
    )
    private List<Prescription> prescriptions;


    // constructors
    public Drug(){}

    public Drug(int id, String drugName, String type) {
        this.id = id;
        this.drugName = drugName;
        this.type = type;
    }

    public Drug(String drugName, String type) {
        this.drugName = drugName;
        this.type = type;
    }


    // getters & setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    // define toString
    @Override
    public String toString() {
        return "Drug{" +
                "id=" + id +
                ", drugName='" + drugName + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
