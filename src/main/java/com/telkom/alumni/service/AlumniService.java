package com.telkom.alumni.service;

import com.telkom.alumni.model.Alumni;
import com.telkom.alumni.model.JobExperience;
import com.telkom.alumni.repository.AlumniRepository;
import com.telkom.alumni.repository.JobExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AlumniService {
    
    @Autowired
    private AlumniRepository alumniRepository;
    
    @Autowired
    private JobExperienceRepository jobExperienceRepository;
    
    public List<Alumni> getAllAlumni() {
        return alumniRepository.findAll();
    }
    
    public Alumni getAlumniById(String id) {
        return alumniRepository.findById(id).orElse(null);
    }
    
    public Alumni getAlumniByEmail(String email) {
        return alumniRepository.findByEmail(email);
    }
    
    public Alumni saveAlumni(Alumni alumni) {
        return alumniRepository.save(alumni);
    }
    
    public void deleteAlumni(String id) {
        alumniRepository.deleteById(id);
    }
    
    public Alumni updateProfile(Alumni alumni) {
        return alumniRepository.save(alumni);
    }
    
    public void addJobExperience(String alumniId, JobExperience job) {
        Alumni alumni = getAlumniById(alumniId);
        if (alumni != null) {
            alumni.addJob(job);
            alumniRepository.save(alumni);
        }
    }
    
    public void deleteJobExperience(String alumniId, int index) {
        Alumni alumni = getAlumniById(alumniId);
        if (alumni != null) {
            alumni.deleteJob(index);
            alumniRepository.save(alumni);
        }
    }
    
    public List<JobExperience> getJobExperiences(String alumniId) {
        return jobExperienceRepository.findByAlumni_IdUser(alumniId);
    }
    
    public List<Alumni> searchAlumniByName(String keyword) {
        return alumniRepository.findByNameContainingIgnoreCase(keyword);
    }
}