package com.telkom.alumni.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "alumni")
public class Alumni extends User {
    
    @Column(name = "enrollment_year")
    private int enrollmentYear;
    
    @Column(name = "major")
    private String major;
    
    @OneToMany(mappedBy = "alumni", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<JobExperience> jobExperience = new ArrayList<>();
    
    public Alumni() {}
    
    public Alumni(String idUser, String name, String email, String password,
                  int enrollmentYear, String major) {
        super(idUser, name, email, password);
        this.enrollmentYear = enrollmentYear;
        this.major = major;
    }
    
    @Override
    public String getProfile() {
        return "=== Profile Alumni ===\n" +
               "Nama     : " + getName() + "\n" +
               "Angkatan : " + enrollmentYear + "\n" +
               "Jurusan  : " + major;
    }
    
    public String displayJobExperience() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Riwayat Pekerjaan: ").append(getName()).append(" ===\n");
        if (jobExperience.isEmpty()) {
            sb.append("Belum ada riwayat pekerjaan.\n");
        } else {
            for (int i = 0; i < jobExperience.size(); i++) {
                sb.append(i+1).append(". ").append(jobExperience.get(i).toString()).append("\n");
            }
        }
        return sb.toString();
    }
    
    public void addJob(JobExperience job) {
        job.setAlumni(this);
        this.jobExperience.add(job);
    }
    
    public void deleteJob(int index) {
        if (index >= 0 && index < jobExperience.size()) {
            jobExperience.remove(index);
            System.out.println("Riwayat pekerjaan berhasil dihapus.");
        } else {
            System.out.println("Index tidak valid.");
        }
    }
    
    // Getters and Setters
    public int getEnrollmentYear() { return enrollmentYear; }
    public void setEnrollmentYear(int enrollmentYear) { this.enrollmentYear = enrollmentYear; }
    
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
    
    public List<JobExperience> getJobExperience() { return jobExperience; }
    public void setJobExperience(List<JobExperience> jobExperience) { this.jobExperience = jobExperience; }
}