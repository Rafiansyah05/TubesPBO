package com.telkom.alumni.model;

import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class CareerStatistic {
    
    private List<Alumni> alumniList;
    
    public CareerStatistic() {}
    
    public CareerStatistic(List<Alumni> alumniList) {
        this.alumniList = alumniList;
    }
    
    public void setAlumniList(List<Alumni> alumniList) {
        this.alumniList = alumniList;
    }
    
    public List<Map<String, Object>> getTopCompanies() {
        return hitungDanTampilTop10(null);
    }
    
    public List<Map<String, Object>> getTopCompaniesByJurusan(String jurusan) {
        return hitungDanTampilTop10(jurusan);
    }
    
    public Map<String, List<Map<String, Object>>> getDistribusiByJurusan() {
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        
        Set<String> jurusanSet = new HashSet<>();
        for (Alumni alumni : alumniList) {
            if (alumni != null) {
                jurusanSet.add(alumni.getMajor());
            }
        }
        
        for (String jurusan : jurusanSet) {
            result.put(jurusan, getTopCompaniesByJurusan(jurusan));
        }
        return result;
    }

    public List<Alumni> getAlumniByJurusan(String jurusan) {
        List<Alumni> result = new ArrayList<>();
        for (Alumni alumni : alumniList) {
            if (alumni != null && alumni.getMajor().equalsIgnoreCase(jurusan)) {
                result.add(alumni);
            }
        }
        return result;
    }
    
    public List<Map<String, Object>> getAlumniByCompany(String namaCompany) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Alumni alumni : alumniList) {
            if (alumni == null) continue;
            for (JobExperience job : alumni.getJobExperience()) {
                if (job != null && job.getCompany().getName().equalsIgnoreCase(namaCompany)) {
                    Map<String, Object> info = new HashMap<>();
                    info.put("name", alumni.getName());
                    info.put("major", alumni.getMajor());
                    info.put("enrollmentYear", alumni.getEnrollmentYear());
                    info.put("jabatan", job.getJabatan());
                    result.add(info);
                    break;
                }
            }
        }
        return result;
    }
    
    private List<Map<String, Object>> hitungDanTampilTop10(String filterJurusan) {
        Map<String, Integer> companyCount = new HashMap<>();
        
        for (Alumni alumni : alumniList) {
            if (alumni == null) continue;
            if (filterJurusan != null && !alumni.getMajor().equalsIgnoreCase(filterJurusan)) continue;
            
            for (JobExperience job : alumni.getJobExperience()) {
                if (job == null) continue;
                String companyName = job.getCompany().getName();
                companyCount.put(companyName, companyCount.getOrDefault(companyName, 0) + 1);
            }
        }
        
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(companyCount.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        List<Map<String, Object>> top10 = new ArrayList<>();
        int limit = Math.min(10, sorted.size());
        for (int i = 0; i < limit; i++) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("company", sorted.get(i).getKey());
            entry.put("count", sorted.get(i).getValue());
            top10.add(entry);
        }
        
        return top10;
    }
}