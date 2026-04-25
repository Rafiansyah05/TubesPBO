package com.telkom.alumni.repository;

import com.telkom.alumni.model.JobExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobExperienceRepository extends JpaRepository<JobExperience, String> {
    List<JobExperience> findByAlumni_IdUser(String alumniId);
}