package com.vheal.entity;

import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

//create table & sync to patient entity in sql workbench
@Component
@Entity
@Table(name="patient")
public class
Patient {

    // fields for patient

    //    primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank(message = "is required")
    @Column(name = "patient_name")
    private String patientName;

    @NotBlank(message = "is required")
    @Pattern(regexp="^[0-9]{10}", message = "enter valid 10 digit number")
    @Column(name = "phone_no")
    private String phoneNo;

    @NotBlank(message = "is required")
    @Range(min = 1, max = 150, message = "enter valid age")
    @Column(name = "age")
    private String age;

    @NotBlank(message = "choose your gender")
    @Column(name = "gender")
    private String gender;

    @NotBlank(message = "is required")
    @Column(name = "city")
    private String city;

    @NotBlank(message = "is required")
    @Column(name = "state")
    private String state;

    @NotBlank(message = "is required")
    @Column(name = "country")
    private String country;

    // One to One Mapping with User entity
    @OneToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    // many to many relationship with doctors
    @ManyToMany(fetch=FetchType.LAZY,
            cascade= {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "doctor_patient",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "doctor_id"))
    private List<Doctor> doctors;


    // one to many relationship with prescription
    @OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.REMOVE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="patient_id")
    private List<Prescription> prescriptions;


    // constructors
    public Patient(){}

    public Patient(String patientName, String phoneNo, String age, String gender,
                   String city, String state, String country) {
        this.patientName = patientName;
        this.phoneNo = phoneNo;
        this.age = age;
        this.gender = gender;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public Patient(int id, String patientName, String phoneNo, String age, String gender,
                   String city, String state, String country) {
        this.id = id;
        this.patientName = patientName;
        this.phoneNo = phoneNo;
        this.age = age;
        this.gender = gender;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    // getters & setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    public List<Prescription> getPrescriptions() {
        if(prescriptions == null ){return prescriptions;}
        List<Prescription> sortedPrescription = new ArrayList<>();
        for(int i = prescriptions.size() - 1; i >= 0; i--){
            sortedPrescription.add(prescriptions.get(i));
        }
        return sortedPrescription;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    // convenience methods
    public void addPrescription(Prescription thePrescription){
        if(prescriptions == null){
            prescriptions = new ArrayList<>();
        }
        prescriptions.add(thePrescription);
    }

    public void deletePrescription(Prescription thePrescription) {
        List<Prescription> newPrescriptions = new ArrayList<>();
        for (Prescription tempPrescription : prescriptions) {
            if(tempPrescription.getId() != thePrescription.getId()){
                newPrescriptions.add(tempPrescription);
            }
        }
        this.prescriptions = newPrescriptions;
    }

    public Doctor getDoctor(){
        if(doctors.isEmpty()){
            return null;
        }
        return doctors.get(0);
    }


    // toString
    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", patientName='" + patientName + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

}
