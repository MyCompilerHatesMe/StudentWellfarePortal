package com.mchm.swp.repo;

import com.mchm.swp.model.profiles.FacultyProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyProfileRepo extends JpaRepository<FacultyProfile, Long> {
}
