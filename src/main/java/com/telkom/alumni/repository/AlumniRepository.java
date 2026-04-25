package com.telkom.alumni.repository;

import com.telkom.alumni.model.Alumni;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlumniRepository extends JpaRepository<Alumni, String> {
    Alumni findByEmail(String email);
    List<Alumni> findByMajor(String major);
    List<Alumni> findByNameContainingIgnoreCase(String name);
}