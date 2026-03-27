package com.mchm.swp.repo;


import com.mchm.swp.model.profiles.ParentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParentProfileRepo extends JpaRepository<ParentProfile, Long> {
    Optional<ParentProfile> findByAuthUser_Username(String authUsername);
}
