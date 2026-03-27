package com.mchm.swp.repo;


import com.mchm.swp.model.profiles.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentProfileRepo extends JpaRepository<StudentProfile, Long> {
    Optional<StudentProfile> findByAuthUser_Username(String username);
}
