package com.telkom.alumni.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "job_experiences")
public class JobExperience {
    
    @Id
    private String idJobExperience;
    
    @Enumerated(EnumType.STRING)
    private Industri industri;
    
    private String jabatan;
    
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    
    @ManyToOne
    @JoinColumn(name = "alumni_id")
    private Alumni alumni;
    
    @Temporal(TemporalType.DATE)
    private Date startDate;
    
    @Temporal(TemporalType.DATE)
    private Date endDate;
    
    public JobExperience() {}
    
    public JobExperience(String idJobExperience, Industri industri,
                         String jabatan, Company company,
                         Date startDate, Date endDate) {
        this.idJobExperience = idJobExperience;
        this.industri = industri;
        this.jabatan = jabatan;
        this.company = company;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    @Override
    public String toString() {
        return jabatan + " di " + company.getName()
            + " | Industri: " + industri
            + " | " + startDate + " s/d " + (endDate != null ? endDate : "sekarang");
    }
    
    public String getIdJobExperience() { return idJobExperience; }
    public void setIdJobExperience(String idJobExperience) { this.idJobExperience = idJobExperience; }
    
    public Industri getIndustri() { return industri; }
    public void setIndustri(Industri industri) { this.industri = industri; }
    
    public String getJabatan() { return jabatan; }
    public void setJabatan(String jabatan) { this.jabatan = jabatan; }
    
    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }
    
    public Alumni getAlumni() { return alumni; }
    public void setAlumni(Alumni alumni) { this.alumni = alumni; }
    
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
}