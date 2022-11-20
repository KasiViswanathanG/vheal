package com.vheal.entity;

import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

//create table & sync to doctor entity in sql workbench
@Component
@Entity
@Table(name="doctor")
public class Doctor {

    // fields for patient

    // primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank(message = "is required")
    @Column(name = "doctor_name")
    private String doctorName;

    @NotBlank(message = "is required")
    @Column(name = "phone_no")
    private String phoneNo;

    @NotBlank(message = "is required")
    @Range(min = 0, max = 150, message = "enter valid age")
    @Column(name = "age")
    private String age;

    @NotBlank(message = "choose your gender")
    @Column(name = "gender")
    private String gender;

    @NotBlank(message = "is required")
    @Column(name = "specialization")
    private String specialization;

    @NotBlank(message = "is required")
    @Column(name = "degree")
    private String degree;

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

    // many to many relationship with patients
    @ManyToMany(fetch=FetchType.LAZY,
            cascade= {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "doctor_patient",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "patient_id"))
    private List<Patient> patients;


    // constructors
    public Doctor(){}

    public Doctor(String doctorName, String phoneNo, String age, String gender,
                  String specialization, String degree,
                  String city, String state, String country) {
        this.doctorName = doctorName;
        this.phoneNo = phoneNo;
        this.age = age;
        this.gender = gender;
        this.specialization = specialization;
        this.degree = degree;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public Doctor(int id, String doctorName, String phoneNo, String age, String gender,
                  String specialization, String degree,
                  String city, String state, String country) {
        this.id = id;
        this.doctorName = doctorName;
        this.phoneNo = phoneNo;
        this.age = age;
        this.gender = gender;
        this.specialization = specialization;
        this.degree = degree;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    // getters/setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
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

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    // convenience methods
    public void addPatient(Patient thePatient){
        if(patients == null){
            patients = new ArrayList<>();
        }
        patients.add(thePatient);
    }

    public void deletePatient(Patient thePatient) {
        List<Patient> newPatients = new ArrayList<>();
        for (Patient tempPatient : patients) {
            if(tempPatient.getId() != thePatient.getId()){
                newPatients.add(tempPatient);
            }
        }
        this.patients = newPatients;
    }


    // toString
    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", doctorName='" + doctorName + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                ", specialization='" + specialization + '\'' +
                ", degree='" + degree + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
