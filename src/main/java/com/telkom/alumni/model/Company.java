package com.telkom.alumni.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
public class Company implements Searching {
    
    @Id
    private String idCompany;
    
    @Column(unique = true, nullable = false)
    private String name;
    
    private String location;
    
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<JobExperience> jobExperiences = new ArrayList<>();
    
    public Company() {}
    
    public Company(String idCompany, String name, String location) {
        this.idCompany = idCompany;
        this.name = name;
        this.location = location;
    }
    
    public int getJumlahAlumni() {
        return jobExperiences.size();
    }
    
    public String getInfo() {
        return "=== Info Perusahaan ===\n" +
               "Nama     : " + name + "\n" +
               "Lokasi   : " + location + "\n" +
               "Jml Alumni Bekerja: " + getJumlahAlumni();
    }
    
    @Override
    public void cekKeyword(String keyword) {
        if (this.name.toLowerCase().contains(keyword.toLowerCase())) {
            System.out.println("Perusahaan ditemukan: " + this.name
                + " | Lokasi: " + location
                + " | Alumni: " + getJumlahAlumni());
        } else {
            System.out.println("Keyword \"" + keyword
                + "\" tidak ditemukan di perusahaan: " + this.name);
        }
    }
    
    public String getIdCompany() { return idCompany; }
    public void setIdCompany(String idCompany) { this.idCompany = idCompany; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public List<JobExperience> getJobExperiences() { return jobExperiences; }
    public void setJobExperiences(List<JobExperience> jobExperiences) { this.jobExperiences = jobExperiences; }
}