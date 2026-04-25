package com.telkom.alumni.repository;

import com.telkom.alumni.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {
    Company findByName(String name);
}