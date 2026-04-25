package com.telkom.alumni.service;

import com.telkom.alumni.model.*;
import com.telkom.alumni.repository.AlumniRepository;
import com.telkom.alumni.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CareerService {
    
    @Autowired
    private AlumniRepository alumniRepository;
    
    @Autowired
    private CompanyRepository companyRepository;
    
    public List<Map<String, Object>> getTopCompanies() {
        CareerStatistic stat = new CareerStatistic(alumniRepository.findAll());
        return stat.getTopCompanies();
    }
    
    public List<Map<String, Object>> getTopCompaniesByMajor(String major) {
        List<Alumni> filtered = alumniRepository.findByMajor(major);
        CareerStatistic stat = new CareerStatistic(filtered);
        return stat.getTopCompaniesByJurusan(major);
    }
    
    public Map<String, List<Map<String, Object>>> getDistribusiByMajor() {
        CareerStatistic stat = new CareerStatistic(alumniRepository.findAll());
        return stat.getDistribusiByJurusan();
    }
    
    public List<Alumni> getAlumniByMajor(String major) {
        return alumniRepository.findByMajor(major);
    }
    
    public List<Map<String, Object>> getAlumniByCompany(String companyName) {
        CareerStatistic stat = new CareerStatistic(alumniRepository.findAll());
        return stat.getAlumniByCompany(companyName);
    }
    
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }
    
    public Company getCompanyByName(String name) {
        return companyRepository.findByName(name);
    }
    
    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }
}