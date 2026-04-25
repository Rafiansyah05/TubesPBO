package com.telkom.alumni.service;

import com.telkom.alumni.model.Admin;
import com.telkom.alumni.model.Alumni;
import com.telkom.alumni.repository.AdminRepository;
import com.telkom.alumni.repository.AlumniRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private AlumniRepository alumniRepository;
    
    @Autowired
    private AlumniService alumniService;
    
    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
    
    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }
    
    public List<Alumni> getAllAlumni() {
        return alumniRepository.findAll();
    }
    
    public void addAlumni(Alumni alumni) {
        alumniRepository.save(alumni);
    }
    
    public void deleteAlumni(String id) {
        alumniRepository.deleteById(id);
    }
    
    public void verifyAlumni(String id) {
        Alumni alumni = alumniService.getAlumniById(id);
        if (alumni != null) {
            System.out.println("Alumni " + alumni.getName() + " berhasil diverifikasi.");
        }
    }
    
    public List<Alumni> searchAlumni(String keyword) {
        return alumniRepository.findByNameContainingIgnoreCase(keyword);
    }
}